# Political and Postal Communities Assignment Findings

### Data Model

- I decided to eliminate the interfaces and change them into classes as I thought at the moment it doesn't bring any value given that there's only one implementation of each of the interface(and can be easily refactored if needed).
- I used a separate converter class for mapping the model data from CSV: CsvToModelConverter, to keep things separately. 
- Postal community CSV data has the correlation with political community and looking by the data one postal code can be linked to multiple political communities. 
Given the queries I had to implement I found it easier to associate in political community the postal codes (CsvToModelConverter#getPostalCommunities)
- Added validations for canton code and district number whenever it was specified in the query. 
Used list of only cantonCode/districtNumber strings so that will be easier to search and validate the provided cantonCode/districtNumber. For it used lombok annotation `@Getter(lazy=true)` so that it will do the calculation only once when it's first called (wasn't really necessary but I found it nice to use it here).

***
Added 2 extra queries that:
 - returns a map with all the cantons and their associated count of districts sorted by the count of districts DESC
 Shows that `Valais` has the highest count of districts `13`
 
 | Canton | Count of districts |
 | ---    | :---: |
 | VS     | 13 | 
 | ZH     | 12 | 
 | AG     | 11 | 
 | GR     | 11 | 
 | BE     | 10 | 
 | SO     | 10 | 
 | VD     | 10 | 
 | SG     | 8 | 
 | TI     | 8 | 
 | FR     | 7 | 
 | LU     | 6 | 
 | SH     | 6 | 
 | SZ     | 6 | 
 | BL     | 5 | 
 | TG     | 5 | 
 | JU     | 3 | 
 | AR     | 3 | 
 | NW     | 1 | 
 | BS     | 1 | 
 | UR     | 1 | 
 | GE     | 1 | 
 | GL     | 1 | 
 | AI     | 1 | 
 | OW     | 1 | 
 | ZG     | 1 | 
 | NE     | 1 |
 
 - returns a map with all the cantons and their associated count of political communities sorted by the count of political communities DESC
 This shows that `Bern` has the highest count of political communities `346`, whereas `Basel-Stadt` and `Glarus` has the lowest count: `3`
 
 | Canton | Count of political communities |
 | ---    | :---: |
 | BE     | 346 | 
 | VD     | 309 | 
 | AG     | 211 | 
 | ZH     | 162 | 
 | FR     | 137 | 
 | VS     | 126 | 
 | TI     | 117 | 
 | SO     | 109 | 
 | GR     | 106 | 
 | BL     | 86 | 
 | LU     | 83 | 
 | TG     | 80 | 
 | SG     | 77 | 
 | JU     | 53 | 
 | GE     | 45 | 
 | NE     | 31 | 
 | SZ     | 30 | 
 | SH     | 26 | 
 | UR     | 20 | 
 | AR     | 20 | 
 | NW     | 11 | 
 | ZG     | 11 | 
 | OW     | 7 | 
 | AI     | 6 | 
 | BS     | 3 | 
 | GL     | 3 |