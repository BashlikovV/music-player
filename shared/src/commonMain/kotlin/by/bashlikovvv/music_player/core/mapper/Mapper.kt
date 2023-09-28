package by.bashlikovvv.music_player.core.mapper

interface Mapper<Entity, Domain> {
    fun fromDomain(domain: Domain): Entity
    fun toDomain(entity: Entity): Domain
}