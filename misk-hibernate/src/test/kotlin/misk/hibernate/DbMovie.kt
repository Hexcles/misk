package misk.hibernate

import misk.hibernate.annotation.Keyspace
import java.time.Instant
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Table
import javax.persistence.Transient

@Entity
@Table(name = "movies")
@Keyspace("movies_sharded")
class DbMovie() : DbRoot<DbMovie>, DbTimestampedEntity {
  @Transient private val transientField: String = "foo"

  @javax.persistence.Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  override lateinit var id: Id<DbMovie>

  @Column
  override lateinit var updated_at: Instant

  @Column
  override lateinit var created_at: Instant

  @Column(nullable = false)
  lateinit var name: String

  @Column(nullable = true)
  var release_date: LocalDate? = null

  constructor(name: String, releaseDate: LocalDate? = null) : this() {
    this.name = name
    this.release_date = releaseDate
  }
}
