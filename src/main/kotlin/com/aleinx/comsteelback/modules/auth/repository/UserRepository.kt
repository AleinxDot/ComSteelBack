package com.aleinx.comsteelback.modules.auth.repository

import com.aleinx.comsteelback.modules.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Int> {
    fun findByUsername(username: String): Optional<User>
}