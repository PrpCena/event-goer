package domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User{
  
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  
  @Column(name = "name", nullable = false)
  private String name;
  
  @Column(name = "email", nullable = false)
  private String email;
  // Relationships to be implemented later
  // TODO: Organized events
  // TODO: Attending events
  // TODO: Staffing events
  
  @CreatedDate
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;
  
  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
