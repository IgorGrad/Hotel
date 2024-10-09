1. CRUD interface for hotel data management. Required hotel data include:

  • Hotel name
  
  • Hotel price
  
  • Hotel geo location

2. Search interface that returns the list of all hotels to the user:

  • Search parameter: My current geo location

  • Output: List of hotels

    o For each hotel, return the name, the price, and the distance from my current location
    
    o The list should be ordered. Hotels that are cheaper and closer to my current location
      should be positioned closer to the top of the list. Hotels that are more expensive and
      further away should be positioned closer to the bottom of the list.

Search interface should return only the hotels prepared through the CRUD interface. You are not
required to use any persistent storage (database or similar), but the design of the application should
enable for easy addition of the persistence layer afterwards. You’ll score bonus points if the search
interface supports paging.
