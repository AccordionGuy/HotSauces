package com.auth0.hotsauces

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/hotsauces")
class HotSauceController(private val hotSauceRepository: HotSauceRepository) {

    @GetMapping("/test")
    fun getHotSauce(): ResponseEntity<String> = ResponseEntity("Yup, it works!",  HttpStatus.OK)

    @GetMapping("")
    fun getAll(@RequestParam(required = false, defaultValue = "") brandNameFilter: String,
               @RequestParam(required = false, defaultValue = "") sauceNameFilter: String,
               @RequestParam(required = false, defaultValue = "") descFilter: String,
               @RequestParam(required = false, defaultValue = "") minHeat: String,
               @RequestParam(required = false, defaultValue = "") maxHeat: String): ResponseEntity<List<HotSauce>> {
        val MAX_SCOVILLE = 3_000_000 // At this point, it's no longer food, but a weapon
        val minHeatFilter = if (minHeat != "") minHeat.toInt() else 0
        val maxHeatFilter = if (maxHeat != "") maxHeat.toInt() else MAX_SCOVILLE
        return ResponseEntity(hotSauceRepository.findAll()
                .filter { it.brandName.contains(brandNameFilter, true) }
                .filter { it.sauceName.contains(sauceNameFilter, true) }
                .filter { it.description.contains(descFilter, true) }
                .filter { it.heat >= minHeatFilter }
                .filter { it.heat <= maxHeatFilter },
                HttpStatus.OK
        )
    }

    @GetMapping("/count")
    fun getCount(): ResponseEntity<Long> = ResponseEntity(hotSauceRepository.count(),
            HttpStatus.OK)

    @GetMapping("/{id}")
    fun getHotSauce(@PathVariable id: Long): ResponseEntity<Optional<HotSauce>> {
        if (hotSauceRepository.existsById(id)) {
            return ResponseEntity(hotSauceRepository.findById(id), HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping()
    fun createHotSauce(@RequestBody hotSauce: HotSauce): ResponseEntity<HotSauce> {
        return ResponseEntity(hotSauceRepository.save(hotSauce), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateHotSauce(@PathVariable id: Long, @RequestBody sauceChanges: HotSauce): ResponseEntity<HotSauce?> {
        if (hotSauceRepository.existsById(id)) {
            val originalSauce = hotSauceRepository.findById(id).get()
            val updatedSauce = HotSauce(
                    id = id,
                    brandName = if (sauceChanges.brandName != "") sauceChanges.brandName else originalSauce.brandName,
                    sauceName = if (sauceChanges.sauceName != "") sauceChanges.sauceName else originalSauce.sauceName,
                    description = if (sauceChanges.description != "") sauceChanges.description else originalSauce.description,
                    url = if (sauceChanges.url != "") sauceChanges.url else originalSauce.url,
                    heat = if (sauceChanges.heat != 0) sauceChanges.heat else originalSauce.heat
            )
            return ResponseEntity(hotSauceRepository.save(updatedSauce), HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteHotSauce(@PathVariable id: Long): ResponseEntity<HotSauce?> {
        if (hotSauceRepository.existsById(id)) {
            hotSauceRepository.deleteById(id)
            return ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

}
