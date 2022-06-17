package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("v1/organization/{organizationId}/license")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable String organizationId,
                                              @PathVariable String licenseId) {
        License license = licenseService.getLicense(licenseId, organizationId);
        LicenseController licenseController = methodOn(LicenseController.class);
        license.add(
                linkTo(licenseController.getLicense(organizationId, license.getLicenseId())).withSelfRel(),
                linkTo(licenseController.createLicense(license, organizationId, null)).withRel("createLicense"),
                linkTo(licenseController.updateLicense(license, organizationId, null)).withRel("updateLicense"),
                linkTo(licenseController.deleteLicense(licenseId, organizationId)).withRel("deleteLicense")
        );
        return ResponseEntity.ok(license);
    }

    @PostMapping
    public ResponseEntity<String> createLicense(@RequestBody License license,
                                                @PathVariable String organizationId,
                                                @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.createLicense(license, organizationId, locale));
    }

    @PutMapping
    public ResponseEntity<String> updateLicense(@RequestBody License license,
                                                @PathVariable String organizationId,
                                                @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.updateLicense(license, organizationId, locale));
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable String licenseId,
                                                @PathVariable String organizationId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, organizationId));
    }
}
