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

package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.PersonPojo;

@Component
public class Person2Pojo
    implements Converter<Person, PersonPojo> {

  @Override
  public PersonPojo convert(Person source) {
    PersonPojo target = new PersonPojo();
    target.setId(source.getId());
    target.setIdNumber(source.getIdNumber());
    target.setFirstName(source.getFirstName());
    target.setLastName(source.getLastName());
    target.setEmail(source.getEmail());
    if (source.getPhone1() != null) {
      target.setPhone1(source.getPhone1());
    }
    if (source.getPhone2() != null) {
      target.setPhone2(source.getPhone2());
    }
    return target;
  }
}
