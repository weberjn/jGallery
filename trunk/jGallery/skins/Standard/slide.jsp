<%@ taglib uri="http://www.jwi.de/jGallery/taglib" prefix="jg" %>

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
<td class="date" width="20%"><jg:if exists="${image.exif.originalDate}">${image.exif.originalDate}</jg:if><jg:else>${image.fileDate}</jg:else></td>
<td class="name">${image.label}</td>

<td class="navigation" width="20%">

<!-- Index button -->
<a href="${folder.indexPage}"><img src="${folder.resPath}/index.gif" border=0 alt="<jg:text>indexPage</jg:text>"></a>

<!-- Previous button -->
<jg:if exists="${folder.previousPage}">
	<a href="${folder.previousPage}"><img src="${folder.resPath}/previous.gif" alt="<jg:text>previousPage</jg:text>" border=0></a>
</jg:if>
<jg:else>
	<img src="${folder.resPath}/previous_disabled.gif" alt="<jg:text>atFirstPage</jg:text>">
</jg:else>

<!-- Next button -->
<jg:if exists="${folder.nextPage}">
	<a href="${folder.nextPage}"><img src="${folder.resPath}/next.gif" alt="<jg:text>nextPage</jg:text>" border=0></a>
</jg:if>
<jg:else>
	<img src="${folder.resPath}/next_disabled.gif" alt="<jg:text>atLastPage</jg:text>">
</jg:else>

</td>
</tr>
</table>
<center>
<table><tr valign="TOP"><td>

<!-- Image, maybe with link to original -->
<jg:if exists="${image.originalPath}">
	<a href="${image.originalPath}">
		<img src="${image.imagePath}" width="${image.imageWidth}" height="${image.imageHeight}" border=0 alt="<jg:text>originalImage</jg:text>">
	</a>
</jg:if>
<jg:else>
	<img src="${image.imagePath}" width="${image.imageWidth}" height="${image.imageHeight}">
</jg:else>

<!-- Always display comment below image (if exists) -->
<jg:if exists="${image.exif.comment}">
	<br>
	<div class="name">${image.exif.comment}</div>
</jg:if>
<jg:else>
	<!-- Try to extract the comment from a file carrying the same base name as this image -->
	<br>
	<div class="name">
		<jsp:include page="${image.label}.txt" />
	</div>
</jg:else>

</td>

<!-- Image info button if camera information exists -->
<jg:if exists="${image.exif.flash}">
	<td>
	<a href="javascript:toggleInfo()"><img src="${folder.resPath}/camera.gif" alt="<jg:text>cameraInfo</jg:text>" border=0></a>
	<br>
	<div class="imageinfo" id="imageinfo" STYLE="visibility:hidden;">
	<table>
		<tr><td>File size</td><td>${image.fileSize}</td></tr>
		<tr><td>Original date</td><td>${image.exif.originalDate}</td></tr>
		<tr><td>Resolution</td><td>${image.exif.resolution}</td></tr>
		<tr><td>Flash</td><td>${image.exif.flash}</td></tr>
		<tr><td>Focal length</td><td>${image.exif.focalLength}</td></tr>
		<tr><td>Exposure time</td><td>${image.exif.exposureTime}</td></tr>
		<tr><td>Aperture</td><td>${image.exif.aperture}</td></tr>
		<tr><td>Focus Distance</td><td>${image.exif.focusDistance}</td></tr>
		<tr><td>Metering Mode</td><td>${image.exif.meteringMode}</td></tr>
		<tr><td>Camera make</td><td>${image.exif.cameraMake}</td></tr>
		<tr><td>Camera model</td><td>${image.exif.cameraModel}</td></tr>
		<tr><td>Sensor type</td><td>${image.exif.sensorType}</td></tr>
	</table></div>
	</td>
</jg:if>

</tr>
</table>
<jg:if exists="${folder.counter}">
		<small>image viewed ${image.counter} times</small>
	</jg:if>
</center>
<a href="${folder.generatorurl}" target="_blank"><small>${folder.generator}</small></a>

</body>
</html>
