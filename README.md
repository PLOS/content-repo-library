Content Repository Library
==================

'InputStream getLatestRepoObjStream(String key)' is replacing 'InputStream getFileInStream(String key)' from the file-store jar

'Map<String, String> createRepoObj(RepoObject repoObject)' is responsible of creating an asset in the content-repo.
The file-store provided the method 'OutputStream getFileOutStream(final String key, long byteCount)' to obtain an OutputStream to write
the new file in the content.

'Boolean deleteLatestRepoObj(String key)'  is replacing void 'deleteFile(String fsid)' from the file-store jar