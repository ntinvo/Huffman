/**  
 
 **/




The write up shall include an estimate of how long you spent on the program and what your results. 
The bulk of the write up will be an analysis of your empirical data. What kinds of file lead to lots of compressions? 
What kind of files had little or no compression? What happens when you try and compress a huffman code file?


How long you spent on the program and what your results? 
--> I spent about 6 days to work on this assignment by myself. The part that took me a lot of times to work
    on was to  decide what classes that I need to make the program work. This assignment is much harder
    than I thought in the first place (especially at read bits and write bit), but finally I finished, and 
    it is working now. I like this assignment but I think I prefer the Anagram Solver better.

What kinds of file lead to lots of compressions?
What kind of files had little or no compression?
What happens when you try and compress a huffman code file?
--> After running one calgary, waterloo, and BooksANDHTML, I got these results: 	
    
    * waterloo:
        total bytes read: 12466304
        total compressed bytes 10205282
        total percent compression 18.137
    
    * calgary:
        total bytes read: 3251493
        total compressed bytes 1845571
        total percent compression 43.239

    * BooksANDHTML:
        total bytes read: 9788581
	total compressed bytes 5828112
	total percent compression 40.460
    
    Also, I tried to run the program with pdf files, jpg files, and png files. Result: 

    * pdf files:
	total bytes read: 910450
	total compressed bytes 908833
	total percent compression 0.178

    * jpg files:
        total bytes read: 5837151
	total compressed bytes 5839994
	total percent compression -0.049

    * png files:
	total bytes read: 2143107
	total compressed bytes 2148744
	total percent compression -0.263

    Finally, I ran to compress a huffman code file. Result:
    
    * ciaFactBook2008.txt.hf:
   	Compress Return: 47678331
	Saved: 552061
	Percent: 0.0115

    * mystery_count_header.bmp.hf
	Compress Return: 2182051
 	Saved: 221629
        Percent: 0.1015

    So, the kind of files that lead to lots of compressions are text files(.txt), html files(.html), htm files(.htm), and
    the files in calgary.The kind of files that had little or no compression are tiff files in waterloo(.tif), jpg files(.jpg),
    png files(.png) and the huffman code files.

    I think the reason that files had little or no compression (tiff files, jpg files, png files, huffman code files, ...) is that
    those files are already compressed. Usually, the files that are already compressed, we can't compress them any more. This is why
    those files don't get much compression when they are compressed. Addition to some files that I mentioned above, we will not get much 
    (or no compression) with gif files, music files (mp3 files, wma, ...), and movie files (avi files, mpg files, ...)
    
   
    