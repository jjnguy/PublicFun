<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="webInterface.HTMLFooter"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../../styles/buttons.css" />
		<link rel="stylesheet" type="text/css" href="../../styles/documentationStyles.css" />
		<link rel="icon" href="../../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../../favicon.ico" />
		<title>jTunes - User Guide</title>
	</head>
	<body>
		<div class="finalPaperFull">
			<h1>User Guide</h1>
			<h2>Introduction</h2>
			<div class="overview">
				Online jTunes an online music library that you can access from anywhere.
				It allows users to add songs to the online library, sort the songs by many attributes, stream the songs straight from the server, and download the songs to the machine they are currently using.
				Please note that Online jTunes is not a file sharing utility.  Users will only be able to view songs that are in their collection.
			</div>
			<h2>Requirements</h2>
			<div class="overview">
				Online jTunes only requirement is a java enabled web browser that supports cookies.
			</div>
			<h2>Creating an Account</h2>
			<div class="overview">
				You have to have an account to use Online jTunes, but don't worry it's easy.  
				Just click the "Create New User" link on the main page.  You'll be brought to a new 
				page where you can enter your username and password.  If your username is already taken, 
				or you enter an invalid password, an error message will be displayed and you can enter 
				your desired username or password again.  Once you submit a valid username and password 
				you will automatically be logged in.  
			</div>			
			<h2>Logging In</h2>
			<div class="overview">
				Once you have an Online jTunes account, you will just need to login when you want to use the system.  On the main page just enter your username and password.  If you enter an invalid username or password and error message will display and you can try again.  Once logged in you can use all the features of Online jTunes described below. (Note: You must have cookies enabled to utilize Online jTunes)
			</div>			
			<h2>Logging Out</h2>
			<div class="overview">
				Once you are done using Online jTunes, you should logout to prevent other people from using your account.  If you forget to logout, don't worry, you will be automatically logged out after 30 minutes of inactivity.  To logout simply click the "Logout" link.  It's that easy!
			</div>
			<h2>Uploading Music to Your Collection</h2>
			<div class="overview">
				In order to listen to music you have to upload some!  Once logged in, click the "Upload Music to the Collection" link.  You will be brought to a page where you can select an mp3 file from your computer to upload.  Don't worry about poorly named files, Online jTunes automatically extracts the data from the mp3's ID3 tag and uses that to categorize it.  Online jTunes currently only supports the use of mp3 music files.
			</div>
			<h2>Searching Your Music Collection</h2>
			<div class="overview">
				Once logged in click the "Search Music Collection" link.  You will be brought to a page where you can enter a term (or terms) to do a simple search or if you like you can enter terms specific to a song's title, artist or album for an advance search.  Once you submit your search a list of all matching songs will be displayed and you will have the option to download, stream or delete each one.  If no matching songs were found, a message will be displayed indicating so and you will be able to submit another search. (Note: You can only search for songs that are in your own music collection)
			</div>
			<h2>Viewing Your Music Collection</h2>
			<div class="overview">
				If you want to view your entire music collection at once instead of searching it, be sure you are logged in and then simply click the "View Music Collection" link.  A list of all your songs will be displayed with options to download, stream or delete each one. (Note: You can only view songs that are in your music collection)
			</div>
			<h2>Streaming Music From Your Collection</h2>
			<div class="overview">
				Once you have completed a search that yielded at least one result or if you are viewing your music collection you can stream any song directly from your web browser.  Each song will have its own "Stream Song" link.  Simply click the link for the song you wish to stream.  You will be brought to a new page where the song can be played from.
			</div>
			<h2>Downloading Music From Your Collection</h2>
			<div class="overview">
				Once you have completed a search that yielded at least one result or if you are viewing your music collection you can download any song listed.  Simply click the "Download Song" link for the song you wish to download.
			</div>
			<h2>Removing Music From Your Collection</h2>
			<div class="overview">
				Once you have completed a search that yielded at least one result or if you are viewing your music collection you can delete any song listed.  Just click the "Delete Song" link for any song and it will be removed from your collection as well as the Online jTunes database.
			</div>
		</div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>