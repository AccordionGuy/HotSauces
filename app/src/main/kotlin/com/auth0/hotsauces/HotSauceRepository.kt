package com.auth0.hotsauces

import org.springframework.data.repository.PagingAndSortingRepository


interface HotSauceRepository: PagingAndSortingRepository<HotSauce, Long>