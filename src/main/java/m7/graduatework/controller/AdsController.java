package m7.graduatework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import m7.graduatework.dto.ad.AdDto;
import m7.graduatework.dto.ad.AdsDto;
import m7.graduatework.dto.ad.CreateOrUpdateAdDto;
import m7.graduatework.dto.ad.FullAdDto;
import m7.graduatework.service.AdsService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
@Validated
@Tag(name = "Объявления", description = "CRUD объявлений, Secured")
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping
    @Operation(summary = "Получить все объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Результат запроса получен")
    })
    public ResponseEntity<AdsDto> getAds() {
        return ResponseEntity.ofNullable(adsService.getAds());
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Добавить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Объявление добавлено"),
            @ApiResponse(responseCode = "401", description = "Нет авторизации"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Данные не найдены")
    })
    public ResponseEntity<AdDto> addAds(@RequestPart(value = "properties") @Valid CreateOrUpdateAdDto properties,
                                        @RequestPart(value = "image") MultipartFile image) {
        return ResponseEntity.ofNullable(adsService.addAds(properties, image));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить полные данные объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Данные получены"),
            @ApiResponse(responseCode = "404", description = "Данные не найдены")
    })
    public ResponseEntity<FullAdDto> getFullAd(@PathVariable @NotNull Long id) {
        return ResponseEntity.ofNullable(adsService.getFullAd(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Объявление удалено"),
            @ApiResponse(responseCode = "401", description = "Нет авторизации"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
    })
    public ResponseEntity<Void> removeAd(@PathVariable @NotNull Long id) {
        return adsService.removeAds(id) != null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявление обновлено"),
            @ApiResponse(responseCode = "401", description = "Нет авторизации"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Данные не найдены")
    })
    public ResponseEntity<AdDto> updateAd(@PathVariable @NotNull Long id,
                                          @RequestBody @Valid CreateOrUpdateAdDto createOrUpdateAdDTO) {
        return ResponseEntity.of(adsService.updateAd(id, createOrUpdateAdDTO));
    }

    @GetMapping("/me")
    @Operation(summary = "Получить объявления авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявления получены"),
            @ApiResponse(responseCode = "401", description = "Нет авторизации")
    })
    public ResponseEntity<AdsDto> getAdsMe() {
        return ResponseEntity.ofNullable(adsService.getAdsMe());
    }

    @PatchMapping(
            value = "/image/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Обновить изображение объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изображение обновлено"),
            @ApiResponse(responseCode = "404", description = "Данные не найдены")
    })
    public ResponseEntity<InputStreamResource> updateAdsImage(@PathVariable @NotNull Long id,
                                                              @RequestParam MultipartFile image) throws IOException {
        Path path = adsService.updateAdsImage(id, image);
        return path != null
                ? ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(Files.size(path))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString())
                .body(new InputStreamResource(Files.newInputStream(path)))
                : ResponseEntity.notFound().build();
    }
}
