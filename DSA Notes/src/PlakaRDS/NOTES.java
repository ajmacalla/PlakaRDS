package PlakaRDS;

public class NOTES {

}

/* regarding the catalog.txt & ratings.txt = gumamit kasi tayo File Handling o File I/O sa java
 * para instead of SQL for database, hindi na tayo mahihirapan mag-connect ng db 
 * kasi txt na lang, notepad notepad tas paste na lang ng file here
 * ni-research ko yan if may other way kasi nakalimutan ko na pano gumamit ng xampp HAHAHAHAHAHA
 */

/* catalog.txt = manually created nilalagay info
 * andito lahat ng pre-defined info ng app
 * mga laman nito:
 * artist name
 * artist image filename
 * album name
 * album cover filename
 * list of songs
 * 
 * paano siya gumagana?
 * may loadCatalog() method tayo sa Data.java di ba, doon binabasa niya yung file
 * kinukuha niya info & nilalagay sa program's data structure like LinkedHashMap at ArrayList
 * 
 * so kapag in-open yung Browse Artist btn,
 * meron nang info na pinapakita ang app
 */

/* ratings.txt = automatically created
 * andito yung user-submitted data
 * every time na mag-rate at comment si user sa isang song, nagssave siya ng info diyan
 * mga laman niya:
 * artist name
 * song title
 * number of stars
 * user comment
 * 
 * paano gumagana?
 * may appendRating() method, ito yung naglalagay ng bagong rating & comment sa file
 * loadRatings() naman nagbabasa ng previously saved ratings kapag ni-run ulit app
 * 
 * bakit hindi siya nawawala?
 * usually kasi di ba kapag nag-run ka, naglagay ka info, inexit mo
 * tapos ni-run mo ulit, nawawala yung data di ba
 * so dito kasi sinesave ang data sa external file natin
 * 
 * so with File Handling,
 * pwede i-save info sa files para available pa rin kahit i-close at re-run ulit ang app
 */
