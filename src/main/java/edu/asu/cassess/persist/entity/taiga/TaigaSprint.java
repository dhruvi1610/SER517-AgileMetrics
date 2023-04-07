package edu.asu.cassess.persist.entity.taiga;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taiga_sprint")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaigaSprint {

  @Id
  @Column(name = "sprint_id")
  private Long sprintId;

  @Column(name = "project_id")
  private Long projectId;

  @Column(name = "course_name")
  private String courseName;

  @Column(name = "team_name")
  private String teamName;

  @Column(name = "sprint_name")
  private String sprintName;

  @Column(name = "is_closed")
  private Boolean isClosed;

  @Column(name = "estimated_start")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate estimatedStart;

  @Column(name = "estimated_finish")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate estimatedFinish;

  @Column(name = "has_custom_attribute_value")
  private Boolean hasCustomAttributeValue;
}