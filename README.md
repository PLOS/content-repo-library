Content Repository Library
==================

'InputStream getLatestAssetInStream(String key)' is replacing 'InputStream getFileInStream(String key)' from the file-store jar

'Map<String, String> createAsset(RepoAsset repoAsset)' is responsible of creating an asset in the content-repo.
The file-store provided the method 'OutputStream getFileOutStream(final String key, long byteCount)' to obtain an OutputStream to write
the new file in the content.

'Boolean deleteLatestAsset(String key)'  is replacing void 'deleteFile(String fsid)' from the file-store jar