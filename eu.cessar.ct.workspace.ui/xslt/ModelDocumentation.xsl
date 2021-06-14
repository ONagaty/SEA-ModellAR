<?xml version='1.0'?>
<xsl:stylesheet version="1.0" 
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
   xmlns:xs="http://www.w3.org/2001/XMLSchema" 
   xmlns:modeldocumentation="http://www.cessar.eu.ModelDocumentation"
   >

   <xsl:template match="/">
		<html>
			<head>
				<title> Model Documentation report</title>
			</head>
			<body>
				<h1>Model Documentation</h1>
				
				<table width="100%" border="1" title="The module configurations">
					<tr bgcolor = "#cccccc" >
						<th width="15%" align = "center">Module Configuration</th>
						<th width="15%" align = "center">Definition</th>
						<th width="15%" align = "center">Description</th>
						<th width="5%" align = "center">Lower Multiplicity</th>
						<th width="5%" align = "center">Upper Multiplicity</th>
					</tr>
					<xsl:for-each select="modeldocumentation:DocElement/contains" >
						<tr>
							<xsl:call-template name="processLink"> 
								<xsl:with-param name="shortName" >
									<xsl:value-of select="@shortName" />
								</xsl:with-param>	
							</xsl:call-template>
							<td> <font color="blue" style="font-weight:bold;font-size:normal"><xsl:value-of select="@type" /></font> </td>
							<td> <font color="blue" style="font-weight:bold;font-size:normal"><xsl:value-of select="instanceOf/@description" /></font> </td>
							<xsl:call-template name="Multiplicity"/> 
						</tr>	
					</xsl:for-each> 
				</table>
				
				<xsl:apply-templates/>
			</body>
		</html>
   </xsl:template>
   
<!-- / forward slash is used to denote a patern that matches the root node of the XML document -->
    <xsl:template match ="contains" >
				<xsl:variable name="shortName">
					<xsl:value-of select="@shortName" />
				</xsl:variable>
				
				<h2><b>Module Configurations:</b>
					<span class="mw-headline" id="{$shortName}"><xsl:value-of select="$shortName"/></span> 
				</h2>
				
				<xsl:call-template name="processConfig"/>
				
				<xsl:for-each select="contains">
					<xsl:variable name="shortName">
						<xsl:value-of select="@shortName" />
					</xsl:variable>
					<xsl:if test="string-length($shortName)">
						<p><b>Information about container:</b>
							<span class="mw-headline" id="{$shortName}"><xsl:value-of select="$shortName"/></span> 
						</p>
					</xsl:if>
					<xsl:call-template name="processContainers"/>
				</xsl:for-each>
			
	</xsl:template>

	<!-- Get the module configurations -->
	<xsl:template name="processConfig">

		<table width="100%" border="1" title="The module configurations">
			<tr bgcolor = "#cccccc" >
				<th width="15%" align = "center">Container Name</th>
				<th width="15%" align = "center">Definition</th>
				<th width="5%" align = "center">Lower Multiplicity</th>
				<th width="5%" align = "center">Upper Multiplicity</th>
			</tr>
			<xsl:call-template name="compute_Container_Row"/> 
		</table>

	</xsl:template >
	
	<!--Get the containers -->	
	<xsl:template name="processContainers">
		<xsl:attribute name="test" />
		
		<xsl:variable name="shortName">
			<xsl:value-of select="@shortName" />
		</xsl:variable>
<p>
		<!-- if the variable is set with a value, only then execute -->		
		<xsl:variable name="atrShortName">
			<xsl:value-of select="attributes/@name" />
		</xsl:variable>
		
		<xsl:if test="string-length($atrShortName)">
			<table width="100%" border="1" title="The Parameters and Attributes of:{$shortName}" >
				<tr bgcolor = "#cccccc" >
					<th width="10%" align = "left">Attribute Name</th>
					<th width="10%" align = "left">Type</th>
					<th width="15%" align = "left">Value</th>
					<th width="15%" align = "left">Definition</th>
					<th width="5%" align = "left">Lower Multiplicity</th>
					<th width="5%" align = "left">Upper Multiplicity</th>
				</tr>
				<xsl:call-template name="compute_Parameter_Row"/>
			</table>
		</xsl:if> 
</p>
<p>
		<!-- if the variable is set with a value, only then execute -->
		<xsl:variable name="childShortName">
			<xsl:value-of select="contains/@shortName" />
		</xsl:variable>
		
		<xsl:if test="string-length($childShortName)">
		
			<table width="100%" border="1" title="The containers which are a child of:{$shortName}" >
				<tr bgcolor = "#cccccc" >
					<th width="15%" align = "left">Container</th>
					<th width="15%" align = "left">Definition</th>
					<th width="5%" align = "left">Lower Multiplicity</th>
					<th width="5%" align = "left">Upper Multiplicity</th>
				</tr>
				<xsl:call-template name="compute_Container_Row"/>
			</table>
		</xsl:if> 
</p>		
		
		<!--Make the container processing recursive -->
		<xsl:for-each select="contains">
			<xsl:variable name="shortName">
				<xsl:value-of select="@shortName" />
			</xsl:variable>
			
			<xsl:if test="string-length($shortName)">
				<p><b>Information about container:</b>
					<span class="mw-headline" id="{$shortName}"><xsl:value-of select="$shortName"/></span> 
				</p>
			</xsl:if>
			
			<xsl:call-template name="processContainers"/>
		</xsl:for-each>
		
	</xsl:template>
		
	<!-- stable stuff -->
	<!-- compute a row .... for a Attribute-->
	<xsl:template name="compute_Parameter_Row">
		
		<xsl:for-each select="attributes" >
			<tr>
				<td> <font color="blue" style="font-weight:bold;font-size:normal"><xsl:value-of select="@name"/></font> </td>
				<td> <font color="blue" style="font-weight:bold;font-size:normal"><xsl:value-of select="@type" /></font> </td>
				<td> <font color="blue" style="font-weight:bold;font-size:normal"><xsl:value-of select="@value" /></font> </td>
				<xsl:call-template name="Definition"/>
				<xsl:call-template name="Multiplicity"/>
			</tr>	
		</xsl:for-each> 
	</xsl:template>
	
		<!-- compute a row .... could be for MC or containers-->
	<xsl:template name="compute_Container_Row">
		
		<xsl:for-each select="contains" >
			<tr>
				<xsl:call-template name="processLink"> 
					<xsl:with-param name="shortName" >
						<xsl:value-of select="@shortName" />
					</xsl:with-param>	
				</xsl:call-template>
				<td> <font color="blue" style="font-weight:bold;font-size:normal"><xsl:value-of select="@type" /></font> </td>
				<xsl:call-template name="Multiplicity"/> 
			</tr>	
		</xsl:for-each> 
	</xsl:template>
	
	<!--create the LINKs-->
	<xsl:template name="processLink">
		<xsl:param name="shortName"/>
		<td>
				<a href="#{$shortName}"> 
					<span class="toctext"> <font color="green" style="font-weight:bold;font-size:normal"><xsl:value-of select="@shortName" /></font> 
					</span>
				</a>
			</td>
	</xsl:template >
	
	<!--compute the multiplicity <td> -->
	<xsl:template name="Multiplicity">
		<xsl:for-each select="instanceOf/multiplicity" >
			<td> <font color="blue" style="font-weight:bold;font-size:normal"><xsl:value-of select="@lower" /></font> </td>
			<td> <font color="blue" style="font-weight:bold;font-size:normal"><xsl:value-of select="@upper" /></font> </td>
		</xsl:for-each>
	</xsl:template >
	<!-- compute the Definition from the Attributes-->
	<xsl:template name="Definition">
		<xsl:for-each select="instanceOf" >
			<td> <font color="blue" style="font-weight:bold;font-size:normal"><xsl:value-of select="@name" /></font> </td>
		</xsl:for-each>
	</xsl:template >
	

</xsl:stylesheet>