build: [![Build Status Badge]][Build Status]&#8193;&#9733;&#8193;
# Content Repository Library
==================

'InputStream getLatestRepoObjStream(String key)' is replacing 'InputStream getFileInStream(String key)' from the file-store jar

'Map<String, String> createRepoObj(RepoObject repoObject)' is responsible of creating an asset in the content-repo.
The file-store provided the method 'OutputStream getFileOutStream(final String key, long byteCount)' to obtain an OutputStream to write
the new file in the content.

'Boolean deleteLatestRepoObj(String key)'  is replacing void 'deleteFile(String fsid)' from the file-store jar

Please contact us at dev@ambraproject.org with any questions, comments, or concerns.
Please use [Github Issues](https://github.com/PLOS/ambraproject/issues) to report any problems, or submit a pull request.

[Build Status]: https://teamcity.plos.org/teamcity/viewType.html?buildTypeId=ContentRepoClientLibrary
[Build Status Badge]: https://teamcity.plos.org/teamcity/app/rest/builds/builType:(id:ContentRepoClientLibrary)/statusIcon.svg
