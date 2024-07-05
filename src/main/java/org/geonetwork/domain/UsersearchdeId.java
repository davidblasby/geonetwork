package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class UsersearchdeId implements Serializable {
  private static final long serialVersionUID = 4994156159426699138L;

  @NotNull
  @Column(name = "iddes", nullable = false)
  private Integer iddes;

  @Size(max = 5)
  @NotNull
  @Column(name = "langid", nullable = false, length = 5)
  private String langid;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    UsersearchdeId entity = (UsersearchdeId) o;
    return Objects.equals(this.iddes, entity.iddes) && Objects.equals(this.langid, entity.langid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iddes, langid);
  }
}