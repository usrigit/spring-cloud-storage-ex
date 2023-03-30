/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST Controller that exposes read and write operations on a Google Cloud Storage file accessed
 * using the Spring Resource Abstraction.
 */
@RestController
public class WebController {

  @Value("gs://${gcs-resource-test-bucket}/my-file.txt")
  private Resource gcsFile;

  @Value("${spring.cloud.gcp.project-id}")
  private String projectId;

  @Value("${spring.cloud.gcp.credentials.location}")
  private String serviceFile;


  @GetMapping("/")
  public String readGcsFile() throws IOException {
    return StreamUtils.copyToString(this.gcsFile.getInputStream(), Charset.defaultCharset()) + "\n";
  }

  @PostMapping("/")
  public String writeGcs(@RequestBody String data) throws IOException {
    try (OutputStream os = ((WritableResource) this.gcsFile).getOutputStream()) {
      os.write(data.getBytes());
    }
    return "file was updated\n";
  }

  /**
   * Check if bucket is available or not
   * If available then return not then create
   * @param data
   * @return
   */
  public String getBucketInfo(@RequestBody String data){

    return createStorageBucket(data);

  }
  private String createStorageBucket(String data) {

    //1. Set credentials and project id to build storage
    Credentials credentials = null;
    Storage storage = null;
    try {
      credentials = GoogleCredentials
              .fromStream(new FileInputStream(serviceFile));
      storage = StorageOptions.newBuilder().setCredentials(credentials)
              .setProjectId(projectId).build().getService();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    //2. Check if bucket is available or not if not hen Create a bucket
    if(storage != null){
      Bucket bucket = storage.create(BucketInfo.of(data));
      return bucket.getName();
    }

    return null;

  }
}
