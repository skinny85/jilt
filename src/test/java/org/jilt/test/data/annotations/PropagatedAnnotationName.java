package org.jilt.test.data.annotations;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public class PropagatedAnnotationName {

  public final String firstName;
  public final String middleName;

  @Nonnull
  @CheckForNull
  public final String lastName;

  public PropagatedAnnotationName(String firstName, String middleName, String lastName) {
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
  }

}
