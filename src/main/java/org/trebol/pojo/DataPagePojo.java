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

package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A simple object that represents a page of data for a specific type
 * @param <T> The type of data in the page
 */
@JsonInclude
public class DataPagePojo<T> {
  private Collection<T> items;
  private int pageIndex;
  private long totalCount;
  private int pageSize;

  public DataPagePojo(int pageIndex, int pageSize) {
    this.items = new ArrayList<>();
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  public DataPagePojo(
    Collection<T> items,
    int pageIndex,
    long totalCount,
    int pageSize
  ) {
    this.items = items;
    this.pageIndex = pageIndex;
    this.totalCount = totalCount;
    this.pageSize = pageSize;
  }

  public Collection<T> getItems() {
    return items;
  }

  public void setItems(Collection<T> items) {
    this.items = items;
  }

  public int getPageIndex() {
    return pageIndex;
  }

  public void setPageIndex(int pageIndex) {
    this.pageIndex = pageIndex;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataPagePojo<?> that = (DataPagePojo<?>) o;
    return pageIndex == that.pageIndex &&
        totalCount == that.totalCount &&
        pageSize == that.pageSize &&
        Objects.equals(items, that.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, pageIndex, totalCount, pageSize);
  }

  @Override
  public String toString() {
    return "DataPagePojo{" +
        "items=" + items +
        ", pageIndex=" + pageIndex +
        ", totalCount=" + totalCount +
        ", pageSize=" + pageSize +
        '}';
  }
}
