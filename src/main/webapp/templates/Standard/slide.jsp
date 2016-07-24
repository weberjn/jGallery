<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=${folder.textEncoding}" />
<title>${image.label}</title>
<link href="${folder.stylePath}" rel=stylesheet>
<script language="javascript">
	showing = false;

	function toggleInfo() {
			if (showing == false) {
				if (document.all || document.getElementById) document.getElementById('imageinfo').style.visibility="visible";	// IE & Gecko
				else document.layers['imageinfo'].visibility="show"; // Netscape 4
				showing = true;
			}
			else {
				if (document.all || document.getElementById) document.getElementById('imageinfo').style.visibility="hidden";	// IE & Gecko
				else document.layers['imageinfo'].visibility="hide";	// Netscape 4
				showing = false;
		}
	}
</script>
</head>

<body id="slide">

<table width="100%">
<tr class="head">
<td class="number" width="10%">${folder.imageNum}/${folder.totalImages}</td>
<td class="date" width="20%">
<c:choose> 
	<c:when test="${!empty image.exif.originalDate}">${image.exif.originalDate}
	</c:when> 
	<c:otherwise>${image.fileDate}</c:otherwise>
</c:choose>
</td>
<td class="name">${image.label}</td>

<td class="navigation" width="20%">

<!-- Index button -->
<a href="<c:url value='${folder.indexPage}'/>"><img src="${folder.resPath}/index.gif" border=0 alt="index Page" title="index Page"></a>

<!-- Previous button -->
<c:choose> 
	<c:when test="${!empty folder.previousPage}">
	<a href="<c:url value='${folder.previousPage}'/>"><img src="${folder.resPath}/previous.gif" alt="previous Page" title="previous Page" border=0></a>
	</c:when> 
	<c:otherwise>
	<img src="${folder.resPath}/previous_disabled.gif" alt="at first page">
</c:otherwise>
</c:choose>

<!-- Next button -->
<c:choose> 
	<c:when test="${!empty folder.nextPage}">
	<a href="<c:url value='${folder.nextPage}'/>"><img src="${folder.resPath}/next.gif" alt="next Page" 
	title="next Page" border=0></a>
	</c:when> 
	<c:otherwise>
	<img src="${folder.resPath}/next_disabled.gif" alt="at last page">
	</c:otherwise>
</c:choose>

</td>
</tr>
</table>
<center>
<table><tr valign="TOP"><td>

<!-- Image, maybe with link to original -->
<c:choose> 
	<c:when test="${!empty image.originalPath}">
	<a href="<c:url value='${image.originalPath}'/>">
		<img src="${image.imagePath}" width="${image.imageWidth}" height="${image.imageHeight}" border=0 alt="original image">
	</a>
	</c:when> 
	<c:otherwise>
	<img src="${image.imagePath}" width="${image.imageWidth}" height="${image.imageHeight}">
</c:otherwise>
</c:choose>

<!-- Always display comment below image (if exists) -->
<c:choose> 
	<c:when test="${!empty image.comment}">
	<br>
	<div class="name">${image.comment}</div>
</c:when> 
	<c:otherwise>
	<!-- Try to extract the comment from a file carrying the same base name as this image -->
	<br>
	<c:if test="${image.hasCommentFile}">
	<div class="name">
		<jsp:include page="${image.label}.txt" />
	</div>
	</c:if> 
</c:otherwise>
</c:choose>

</td>

<!-- Image info button if camera information exists -->
<c:if test="${!empty image.exif.flash}">
	<td>
	<a href="javascript:toggleInfo()"><img src="${folder.resPath}/camera.gif" alt="text>camera info" border=0></a>
	<br>
	<div class="imageinfo" id="imageinfo" STYLE="visibility:hidden;">
	<table>
		<tr><td>File size</td><td>${image.fileSize}</td></tr>
		<tr><td>Original date</td><td>${image.exif.originalDate}</td></tr>
		<tr><td>Flash</td><td>${image.exif.flash}</td></tr>
		<tr><td>Focal length</td><td>${image.exif.focalLength}</td></tr>
		<tr><td>Exposure time</td><td>${image.exif.exposureTime}</td></tr>
		<tr><td>Aperture</td><td>${image.exif.aperture}</td></tr>
		<tr><td>ISO</td><td>${image.exif.isoEquivalent}</td></tr>
		<tr><td>Metering Mode</td><td>${image.exif.meteringMode}</td></tr>
		<tr><td>Camera model</td><td>${image.exif.cameraModel}</td></tr>
	</table></div>
	</td>
</c:if>

</tr>
</table>
<c:if test="${!empty image.counter}">
		<small>image viewed ${image.counter} times</small>
	</c:if>
</center>
<a href="${folder.generatorurl}" target="_blank"><small>${folder.generator}</small></a>

</body>
</html>
