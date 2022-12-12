package com.jpmc.theater;

import java.util.Objects;

public class Customer {

  private final String name;

  private final String id;

  public Customer(String name, String id) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(id);
    this.id = id; // NOTE - id is not used anywhere at the moment
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Customer customer = (Customer) o;
    return name.equals(customer.name) && id.equals(customer.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, id);
  }

  @Override
  public String toString() {
    return "name: " + name;
  }
}
