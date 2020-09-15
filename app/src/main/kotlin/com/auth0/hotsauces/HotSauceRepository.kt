package com.auth0.hotsauces

import org.springframework.data.repository.CrudRepository


interface HotSauceRepository: CrudRepository<HotSauce, Long>