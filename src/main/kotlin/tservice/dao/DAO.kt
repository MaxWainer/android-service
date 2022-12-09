package tservice.dao

import tservice.struct.User
import tservice.struct.Users
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDAO(): UserDAO = UserDAOImpl()

private class UserDAOImpl : UserDAO {
    init {
        val database = Database.connect("jdbc:h2:file:./build/database", "org.h2.Driver")
        transaction(database) {
            SchemaUtils.create(Users)

            if (Users.selectAll().empty()) {
                Users.insert {
                    it[name] = "Artyom"
                    it[surname] = "Alexeev"
                    it[age] = 19
                }

                Users.insert {
                    it[name] = "Clown"
                    it[surname] = "Clownwich"
                    it[age] = 5
                }

                Users.insert {
                    it[name] = "Ivan"
                    it[surname] = "Egg"
                    it[age] = 15
                }
            }
        }
    }

    override suspend fun getByName(name: String) = query {
        Users.select { Users.name eq name }
            .map(User::wrapRow)
            .firstOrNull() ?: throw BadRequestException("Unknown user with namee: $name")
    }

    override suspend fun getById(id: Int) = query {
        Users.select { Users.id eq id }
            .map(User::wrapRow)
            .firstOrNull() ?: throw BadRequestException("Unknown user with id: $id")
    }

    private suspend fun query(query: suspend () -> User) = newSuspendedTransaction(Dispatchers.IO) { query() }
}