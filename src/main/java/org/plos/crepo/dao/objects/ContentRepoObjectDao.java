/*
 * Copyright 2017 Public Library of Science
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.plos.crepo.dao.objects;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.plos.crepo.model.RepoObject;

public interface ContentRepoObjectDao {

  CloseableHttpResponse getLatestRepoObj(String bucketName, String key);

  CloseableHttpResponse getRepoObjUsingUuid(String bucketName, String key, String uuid);

  CloseableHttpResponse getRepoObjUsingVersionNum(String bucketName, String key, int versionNumber);

  CloseableHttpResponse getRepoObjMetaLatestVersion(String bucketName, String key);

  CloseableHttpResponse getRepoObjMetaUsingUuid(String bucketName, String key, String uuid);

  CloseableHttpResponse getRepoObjMetaUsingVersionNumber(String bucketName, String key, int versionNumber);

  CloseableHttpResponse getRepoObjVersionsMeta(String bucketName, String key);

  CloseableHttpResponse getRepoObjMetaUsingTag(String bucketName, String key, String tag);

  CloseableHttpResponse deleteRepoObjUsingUuid(String bucketName, String key, String uuid);

  CloseableHttpResponse deleteRepoObjUsingVersionNumber(String bucketName, String key, int versionNumber);

  CloseableHttpResponse createRepoObj(String bucketName, RepoObject repoObject, String contentType);

  CloseableHttpResponse versionRepoObj(String bucketName, RepoObject repoObject, String contentType);

  CloseableHttpResponse autoCreateRepoObj(String bucketName, RepoObject repoObject, String contentType);

  CloseableHttpResponse getRedirectURL(String bucketName, String key);

  CloseableHttpResponse getObjects(String bucketName, int offset, int limit, boolean includeDeleted);

  CloseableHttpResponse getObjectsUsingTag(String bucketName, int offset, int limit, boolean includeDeleted, String tag);

}
