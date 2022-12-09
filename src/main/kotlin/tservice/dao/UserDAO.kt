package tservice.dao

import tservice.struct.User

interface UserDAO {
    suspend fun getByName(name: String): User
    suspend fun getById(id: Int): User
}