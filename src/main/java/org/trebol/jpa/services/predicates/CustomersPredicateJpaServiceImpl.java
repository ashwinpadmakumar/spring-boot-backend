/*
 * Copyright (c) 2022 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.QCustomer;
import org.trebol.jpa.entities.QPerson;
import org.trebol.jpa.services.IPredicateJpaService;

import java.util.Map;

@Service
public class CustomersPredicateJpaServiceImpl
  implements IPredicateJpaService<Customer> {

  private final Logger logger = LoggerFactory.getLogger(CustomersPredicateJpaServiceImpl.class);

  @Override
  public QCustomer getBasePath() {
    return QCustomer.customer;
  }

  @Override
  public Predicate parseMap(Map<String, String> queryParamsMap) {
    QPerson personPath = getBasePath().person;
    BooleanBuilder predicate = new BooleanBuilder();
    for (Map.Entry<String, String> entry : queryParamsMap.entrySet()) {
      String paramName = entry.getKey();
      String stringValue = entry.getValue();
      try {
        switch (paramName) {
          case "id":
            return getBasePath().id.eq(Long.valueOf(stringValue));
          case "idNumber":
            return personPath.idNumber.eq(stringValue);
          case "name":
            predicate.and(personPath.firstName.eq(stringValue)
                    .or(personPath.lastName.eq(stringValue)));
            break;
          case "firstName":
            predicate.and(personPath.firstName.eq(stringValue));
            break;
          case "lastName":
            predicate.and(personPath.lastName.eq(stringValue));
            break;
          case "email":
            predicate.and(personPath.email.eq(stringValue));
            break;
          case "nameLike":
            predicate.and(personPath.firstName.likeIgnoreCase("%" + stringValue + "%")
                    .or(personPath.lastName.likeIgnoreCase("%" + stringValue + "%")));
            break;
          case "firstNameLike":
            predicate.and(personPath.firstName.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "lastNameLike":
            predicate.and(personPath.lastName.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idNumberLike":
            predicate.and(personPath.idNumber.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "emailLike":
            predicate.and(personPath.email.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }
}
