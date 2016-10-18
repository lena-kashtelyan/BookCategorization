# BookCategorization
A program to categorize book by genres based on keywords in their descriptions<br />
Elena Kashtelyan<br />
Brown University, Sc.B. in Computer Science, Class of 2018<br />

I. I wrote the project in Eclipse, so although it should also compile in any other environment, it would probably be easiest to run it from Eclipse. The project folder includes all necessary files and dependencies, so the only configuration needed would be the arguments: the CSV and the JSON file, in that respective order. The file to be run is called Main.java.<br />

II. One of the trade-off I ran into was the tradeoff between faster text processing and more careful word lookup. What I mean by that is that initially I just looped through each book description once, word-by-word, looking for words matching the keywords and twograms. However, I then realized that this procedure overlooked cases where some word contained a keyword within it: such as 'fighting' containing 'fight'. Then I had to switch to using the indexOf() procedure to find keywords in text, and instead of being linear in the number of words in a given body of text, it is now O(N*M) where N is the size of the text body, and M is the number of keywords. (This is why I have two separate hashmaps for single- and double-word keywords, which became unnecessary once I changed my approach.)
Another trade-off I thought of was parsing the CSV file and putting all its contents into memory at once versus using an external library to do lookups in the CSV file directly. However, loading the whole contents into memory seemed reasonable here, as I could not find a library that was both free and did constant-time CSV lookups. I think SQL could be used to serve this purpose if I had more time! Also, with very large CSV and JSON files, the processing would most likely have to happen in chunks.

