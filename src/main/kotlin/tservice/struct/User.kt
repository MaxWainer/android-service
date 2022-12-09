package tservice.struct

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable() {
    val name = varchar("name", 50)
    val surname = varchar("surname", 50)
    val age = integer("age")

    init {
        index(true, name)
    }
}

@Serializable(UserSerializer::class)
class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var surname by Users.surname
    var age by Users.age
}

object UserSerializer : KSerializer<User> {
    override val descriptor = buildClassSerialDescriptor("User") {
        element("id", PrimitiveSerialDescriptor("id", PrimitiveKind.INT))
        element("name", PrimitiveSerialDescriptor("name", PrimitiveKind.STRING))
        element("surname", PrimitiveSerialDescriptor("surname", PrimitiveKind.STRING))
        element("age", PrimitiveSerialDescriptor("age", PrimitiveKind.INT))
    }

    override fun deserialize(decoder: Decoder): User {
        throw NotImplementedError("deserializing is not supported!")
    }

    override fun serialize(encoder: Encoder, value: User) {
        val struct = encoder.beginStructure(descriptor)
        struct.encodeIntElement(descriptor, 0, value.id.value)
        struct.encodeStringElement(descriptor, 1, value.name)
        struct.encodeStringElement(descriptor, 2, value.surname)
        struct.encodeIntElement(descriptor, 3, value.age)
        struct.endStructure(descriptor)
    }
}