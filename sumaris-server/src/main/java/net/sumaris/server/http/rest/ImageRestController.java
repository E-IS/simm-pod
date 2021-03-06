package net.sumaris.server.http.rest;

/*-
 * #%L
 * SUMARiS:: Server
 * %%
 * Copyright (C) 2018 SUMARiS Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import net.sumaris.core.service.administration.DepartmentService;
import net.sumaris.core.service.administration.PersonService;
import net.sumaris.core.service.technical.SoftwareService;
import net.sumaris.core.vo.data.ImageAttachmentVO;
import net.sumaris.core.vo.technical.SoftwareVO;
import net.sumaris.server.config.SumarisServerConfiguration;
import net.sumaris.server.config.SumarisServerConfigurationOption;
import net.sumaris.server.service.administration.ImageService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class ImageRestController implements ResourceLoaderAware {

    /* Logger */
    private static final Logger log = LoggerFactory.getLogger(ImageRestController.class);

    @Autowired
    private PersonService personService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private SoftwareService softwareService;

    @Autowired
    private SumarisServerConfiguration config;

    private ResourceLoader resourceLoader;

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @ResponseBody
    @RequestMapping(value = RestPaths.PERSON_AVATAR_PATH, method = RequestMethod.GET,
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getPersonAvatar(@PathVariable(name="pubkey") String pubkey) {
        ImageAttachmentVO image;
        try {
            image = personService.getAvatarByPubkey(pubkey);
        }
        catch(DataRetrievalFailureException e) {
            image = null;
        }
        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = Base64.decodeBase64(image.getContent());
        return ResponseEntity.ok()
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(bytes);

    }

    @ResponseBody
    @RequestMapping(value = RestPaths.DEPARTMENT_LOGO_PATH, method = RequestMethod.GET,
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getDepartmentLogo(@PathVariable(name="label") String label) {
        ImageAttachmentVO image = departmentService.getLogoByLabel(label);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = Base64.decodeBase64(image.getContent());
        return ResponseEntity.ok()
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(bytes);
    }

    @ResponseBody
    @RequestMapping(value = RestPaths.IMAGE_PATH, method = RequestMethod.GET,
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getImage(@PathVariable(name="id") String id) {
        ImageAttachmentVO image = imageService.get(Integer.parseInt(id));
        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = Base64.decodeBase64(image.getContent());
        return ResponseEntity.ok()
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(bytes);
    }

    @ResponseBody
    @RequestMapping(value = RestPaths.FAVICON, method = RequestMethod.GET,
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public Object getFavicon() {

        SoftwareVO software = softwareService.getDefault();
        if (software == null) return ResponseEntity.notFound().build();

        String favicon = MapUtils.getString(software.getProperties(), SumarisServerConfigurationOption.SITE_FAVICON.getKey());
        if (StringUtils.isBlank(favicon)) {
            return ResponseEntity.notFound().build();
        }

        if (favicon.startsWith(ImageService.URI_IMAGE_SUFFIX)) {
            String imageId = favicon.substring(ImageService.URI_IMAGE_SUFFIX.length());
            return getImage(imageId);
        }

        // Redirect to the URL
        if (favicon.startsWith("http")) {
            return new RedirectView(favicon);
        }

        // Try to read as a local resource
        try {
            Resource faviconResource = resourceLoader.getResource(favicon);
            InputStream in = faviconResource.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            IOUtils.copy(in, bos);
            bos.close();
            in.close();

            return ResponseEntity.ok()
                    .contentLength(faviconResource.contentLength())
                    .body(bos.toByteArray());
        } catch(IOException e) {
            // Not a local resource: continue
        }

        // Redirect as a relative URL
        return new RedirectView(config.getServerUrl() + (favicon.startsWith("/") ? "" : "/") + favicon);

    }
}
