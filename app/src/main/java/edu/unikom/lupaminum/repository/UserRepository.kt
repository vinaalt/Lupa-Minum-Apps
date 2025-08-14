package edu.unikom.lupaminum.repository

import edu.unikom.lupaminum.model.Identity
import edu.unikom.lupaminum.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {
    private var identity: Identity? = null

    fun saveIdentity(id: String) {
        identity = Identity(id)
    }

    fun getIdentity(): Identity? = identity

    fun isIdentitySaved(): Boolean = identity != null
}