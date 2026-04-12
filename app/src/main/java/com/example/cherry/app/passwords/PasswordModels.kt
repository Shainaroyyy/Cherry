package com.example.cherry.app.passwords

data class Account(
    val id: Int,
    val platform: String,
    val username: String,
    val password: String,
    val notes: String = ""
)

object AccountRepository {
    val accounts = mutableListOf(
        Account(1, "Instagram", "vania@email.com", "mypassword123", "Personal account"),
        Account(2, "Gmail", "vania@gmail.com", "gmail_pass456", "Main email"),
        Account(3, "Spotify", "vania_music", "spotify789", "Student plan"),
    )

    fun addAccount(account: Account) = accounts.add(account)
    fun deleteAccount(id: Int) = accounts.removeAll { it.id == id }
    fun updateAccount(updated: Account) {
        val index = accounts.indexOfFirst { it.id == updated.id }
        if (index != -1) accounts[index] = updated
    }
    fun getAccount(id: Int) = accounts.find { it.id == id }
}