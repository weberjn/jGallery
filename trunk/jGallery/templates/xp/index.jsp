
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">



<link rel="stylesheet" type="text/css" href="${folder.skinPath}/main.css">


<head>

<title>${folder.name}</title>



</head>

<body>


<div id="crumb">
You are here:

<c:forEach var="parent" items="${folder.parentFolderList}">

<a href="${parent.url}">${parent.name}</a>

	           &gt;               

</c:forEach>

${folder.name}

</div>


<h1 style="margin-bottom: 0px;">${folder.name}</h1>
<p style="margin-top: 0pt;"> by ${folder.copyright}</p>




<div class="sgShadow"><table class="sgShadow" cellspacing="0">
  <tr>
    <td><img src="${folder.skinPath}/images/shadow-tabl.gif" alt="" /></td>
    <td class="tabm"><table class="sgShadowTab" cellspacing="0"><tr><td>
  
    Showing ${folder.firstImage}-${folder.lastImage} of ${folder.totalImages}  <c:if test="${!empty folder.parentIndexPage}"> 
    	| <a href="${folder.parentIndexPage}">Up</a>  
  </c:if>  
    
    <c:if test="${!empty folder.previousIndexPage}"> 
    	| <a href="${folder.previousIndexPage}">Previous</a>  
  </c:if> 

<c:if test="${!empty folder.nextIndexPage}"> 
    	| <a href="${folder.nextIndexPage}">Next</a> 
  </c:if> 

  
    </td><td><img src="${folder.skinPath}/images/shadow-tabr.gif" alt="" /></td></tr></table></td>
    <td class="tabr"><img src="${folder.skinPath}/images/blank.gif" alt="" /></td>
  </tr>
  <tr>
    <td class="tl"><img src="${folder.skinPath}/images/blank.gif" width="16" height="16" alt="" /></td>
    <td class="tm"><img src="${folder.skinPath}/images/blank.gif" alt="" /></td>
    <td class="tr"><img src="${folder.skinPath}/images/blank.gif" alt="" /></td>
  </tr>
  <tr>
    <td class="ml"><img src="${folder.skinPath}/images/blank.gif" alt="" /></td>
    <td class="mm">
  
    <c:forEach var="image" items="${folder.images}">
    <div class="sgThumbnail">
      <div class="sgThumbnailContent">
        <img class="borderTL" src="${folder.skinPath}/images/slide-tl.gif" alt="" />
        <img class="borderTR" src="${folder.skinPath}/images/slide-tr.gif" alt="" />
        
        <table><tr><td>
          	<a href="${image.closeupPath}">
			<c:choose> 
				<c:when test="${image.iconPath}"> 
					<img class="sgPreviewThumb"
					src="${image.iconPath}" 
					width="${image.thumbWidth}" height="${image.thumbHeight}">
				</c:when> 
				<c:otherwise> 
					<img class="sgPreviewThumb"
					src="${image.thumbPath}" 
					width="${image.thumbWidth}" height="${image.thumbHeight}" title="click to see large view">
				</c:otherwise> 
			</c:choose>  
		</a>
        </td></tr></table>
        
        <div class="roundedCornerSpacer">&nbsp;</div>
      </div>
      <div class="bottomCorners">
        <img class="borderBL" src="${folder.skinPath}/images/slide-bl.gif" alt="" />
        <img class="borderBR" src="${folder.skinPath}/images/slide-br.gif" alt="" />
      </div>
    </div>
     </c:forEach>
  
    </td>
    <td class="mr"><img src="${folder.skinPath}/images/blank.gif" alt="" /></td>
  </tr>
  <tr>
    <td class="bl"><img src="${folder.skinPath}/images/blank.gif" alt="" /></td>
    <td class="bm"><img src="${folder.skinPath}/images/blank.gif" alt="" /></td>
    <td class="br"><img src="${folder.skinPath}/images/blank.gif" width="32" height="32" alt="" /></td>
  </tr>
</table></div>



<div id="footer">
<p> <a href="${folder.generatorurl}" target="_blank"><small>Powered by ${folder.generator}</small></a></p>


<c:if test="${!empty folder.counter}"> 
    	<small>folder viewed ${folder.counter} times</small>
  </c:if>  
</div>


</body></html>