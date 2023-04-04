/*
 * Copyright (C) 2016-2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shio.api.staticfile;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shio.api.ShJsonView;
import com.viglet.shio.bean.error.ShHttpMessageBean;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.object.impl.ShObjectImpl;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.impl.ShPostImpl;
import com.viglet.shio.persistence.repository.folder.ShFolderRepository;
import com.viglet.shio.persistence.repository.object.ShObjectRepository;
import com.viglet.shio.utils.ShStaticFileUtils;
import com.viglet.shio.utils.ShUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Alexandre Oliveira
 */
@RestController
@RequestMapping("/api/v2/staticfile")
@Tag(name = "Static File", description = "Static File API")
public class ShStaticFileAPI {
  static final Logger logger = LogManager.getLogger(ShStaticFileAPI.class.getName());
  @Autowired private ShFolderRepository shFolderRepository;
  @Autowired private ShStaticFileUtils shStaticFileUtils;
  @Autowired private ShObjectRepository shObjectRepository;
  @Autowired private ResourceLoader resourceloader;
  @Autowired private ShUtils shUtils;

  @GetMapping("/pre-upload/{folderId}/{fileName}")
  @JsonView({ShJsonView.ShJsonViewObject.class})
  public ResponseEntity<ShHttpMessageBean> shStaticFilePreUpload(
      @PathVariable String fileName, @PathVariable String folderId) {

    ShFolder shFolder = shFolderRepository.findById(folderId).orElse(null);
    if (!shStaticFileUtils.fileExists(shFolder, fileName)) {

      ShHttpMessageBean shHttpMessageBean = new ShHttpMessageBean();
      shHttpMessageBean.setTitle("File doesn't exists");
      shHttpMessageBean.setMessage("File doesn't exists");
      shHttpMessageBean.setCode(200);

      return new ResponseEntity<>(shHttpMessageBean, HttpStatus.OK);
    } else {

      ShHttpMessageBean shHttpMessageBean = new ShHttpMessageBean();
      shHttpMessageBean.setTitle("File Exists");
      shHttpMessageBean.setMessage("Verify the path of file or change name of file.");
      shHttpMessageBean.setCode(1001);

      return new ResponseEntity<>(shHttpMessageBean, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/upload")
  @JsonView({ShJsonView.ShJsonViewObject.class})
  public ResponseEntity<Object> shStaticFileUpload(
      @RequestParam("file") MultipartFile file,
      @RequestParam("folderId") String folderId,
      @RequestParam("createPost") boolean createPost,
      Principal principal) {

    ShFolder shFolder = shFolderRepository.findById(folderId).orElse(null);
    if (!shStaticFileUtils.fileExists(shFolder, file.getOriginalFilename())) {
      return new ResponseEntity<>(
          shStaticFileUtils.createFilePost(
              file,
              shUtils.sanitizedString(file.getOriginalFilename()),
              shFolder,
              principal,
              createPost),
          HttpStatus.OK);
    } else {

      ShHttpMessageBean shHttpMessageBean = new ShHttpMessageBean();
      shHttpMessageBean.setTitle("File Exists");
      shHttpMessageBean.setMessage("Verify the path of file or change name of file.");
      shHttpMessageBean.setCode(1001);
      return new ResponseEntity<>(shHttpMessageBean, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}/thumbnail")
  public void resize(
      HttpServletRequest request, HttpServletResponse response, @PathVariable String id) {
    ShObjectImpl shObject = shObjectRepository.findById(id).orElse(null);
    response.setContentType(MediaType.IMAGE_PNG_VALUE);
    if (shObject instanceof ShPost) {
      List<String> extensions = new ArrayList<>();
      extensions.add("png");
      extensions.add("jpg");
      extensions.add("jpeg");
      extensions.add("gif");
      File filePath = shStaticFileUtils.filePath((ShPostImpl) shObject);
      String extension = FilenameUtils.getExtension(filePath.getAbsolutePath());

      if (extensions.contains(extension.toLowerCase())) {
        try {
          Thumbnails.of(shStaticFileUtils.filePath((ShPostImpl) shObject))
              .size(230, 230)
              .outputFormat("png")
              .outputQuality(1)
              .toOutputStream(response.getOutputStream());
        } catch (IOException e) {

          logger.error("Image Resize", e);
        }
      } else {
        try {
          Thumbnails.of(
                  resourceloader.getResource("classpath:/ui/public/img/file.png").getInputStream())
              .scale(1)
              .outputFormat("png")
              .outputQuality(1)
              .toOutputStream(response.getOutputStream());
        } catch (IOException e) {
          logger.error("No Image Resize", e);
        }
      }
    }
  }
}
