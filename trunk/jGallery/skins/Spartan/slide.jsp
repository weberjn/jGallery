<%@ taglib uri="http://www.jwi.de/jGallery/taglib" prefix="jg" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta content="text/html; charset=ISO-8859-1"
 http-equiv="content-type">
  <title>${folder.title}</title>
  <link href="${folder.stylePath}" rel=stylesheet>
</head>
<body>

<a href="${folder.indexPage}"> ${folder.title} </a> / (Image ${folder.imageNum}/${folder.totalImages})


<!-- Previous Slide -->
<jg:if exists="${folder.previousPage}">
	<a href="${folder.previousPage}">&lt;&lt;</a>
</jg:if>

<!-- Next Slide -->
<jg:if exists="${folder.nextPage}">
	<a href="${folder.nextPage}">
	&gt;&gt;</a>
</jg:if>

<center>
<table>
<tr>

<td>

<img src="${image.imagePath}" width="${image.imageWidth}" height="${image.imageHeight}">

</td>


</tr>

</table>


</center>
<a href="${folder.generatorurl}" target="_blank"><small>generated by ${folder.generator}</small></a>
</body>
</html>
