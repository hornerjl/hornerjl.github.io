//============================================================================
// Name        : HashTable.cpp
// Author      : Jamie Horner
// Version     : 1.0
//============================================================================

#include <algorithm>
#include <climits>
#include <iostream>
#include <string>
#include <time.h>
#include <vector>
#include <unordered_map>

#include "CSVparser.hpp"

using namespace std;

//============================================================================
// Global definitions visible to all methods and classes
//============================================================================

/*
 * Define a structure to hold bid information
 */
struct Bid {
    unordered_map<string, string> bid;

    Bid(
        string bidId,
        string title,
        string closingDate,
        string department,
        string winningBid,
        string fund,
        string inventoryId
    ) {
        bid["bidId"] = bidId;
        bid["title"] = title;
        bid["closingDate"] = closingDate;
        bid["department"] = department;
        bid["winningBid"] = winningBid;
        bid["fund"] = fund;
        bid["inventoryId"] = inventoryId;
    }

    /**
     * Display the bid information to the console (std::cout)
     */
    void displayBid() {
        cout << bid["bidId"] << ": "
            << bid["title"] << " | "
            << bid["closingDate"] << " | "
            << bid["department"] << " | "
            << bid["winningBid"] << " | "
            << bid["fund"] << " | "
            << bid["inventoryId"] << endl;
        return;
    }
};

/**
 * Define a class containing data members and methods to
 * implement a hash table with chaining.
 */
class HashTable {

private:
    static const unsigned int hashSize = 179;
    string sortBy;
    vector<Bid> bunchOfBids[hashSize];
    unsigned int hash(string key);

public:
    HashTable();
    virtual ~HashTable();
    void setSortBy(string sortBy);
    void Insert(Bid bid);
    void PrintAll();
    vector<Bid> Remove(string searchTerm);
    vector<Bid> Search(string searchTerm);
};

int displayMenu();
void createHashTable(string csvPath, HashTable* bidTable);
void loadBids(string csvPath, HashTable* bidTable);
void removeBids(HashTable* bidTable);
void searchBids(HashTable* bidTable);
void printOperationResult(vector<Bid> bids);

//============================================================================
// Hash Table class function definitions
//============================================================================

/**
 * Default constructor
 */
HashTable::HashTable() {
    sortBy = "bidId";
}

/**
 * Destructor
 */
HashTable::~HashTable() {
	bunchOfBids->clear();
}

/**
 * set sort by to user selection
 *
 * @param sortBy user chosen sort method
 */
void HashTable::setSortBy(string sortBy) {
    this->sortBy = sortBy;
}

/**
 * Calculate the hash value of a given key.
 * Note that key is specifically defined as
 * unsigned int to prevent undefined results
 * of a negative list index.
 *
 * @param key The key to hash
 * @return The calculated hash
 */
unsigned int HashTable::hash(string key) {
    unsigned int hashedKey = 0;
    for (int i = 0; i < key.length(); i++) {
        hashedKey += key[i];
    }
	return (hashedKey % hashSize);
}

/**
 * Insert a bid
 *
 * @param newBid The bid to insert
 */
void HashTable::Insert(Bid newBid) {
	unsigned int key = hash(newBid.bid[sortBy]);
	bunchOfBids[key].push_back(newBid);
}

/**
 * Print all bids
 */
void HashTable::PrintAll() {
	for (int i = 0; i < hashSize; i++){
	    if (bunchOfBids[i].size() != 0){
	        bunchOfBids[i][0].displayBid();
            for (Bid j : bunchOfBids[i]){
                cout << "Key ";
                cout << i << ": ";
                j.displayBid();
            }
	    }
	}
}

/**
 * Remove bids
 *
 * @param searchTerm The bid id to search for
 */
vector<Bid> HashTable::Remove(string searchTerm) {
    vector<Bid> bids;
	unsigned int hashKey = hash(searchTerm);

	int i = 0;
	for (Bid j : bunchOfBids[hashKey]){
		if (j.bid[sortBy] == searchTerm){
		    bids.push_back(bunchOfBids[hashKey].at(i));
			bunchOfBids[hashKey].erase(bunchOfBids[hashKey].begin()+ i);
			i++;
		}
	}

	return bids;
}

/**
 * Search for the specified bidId
 *
 * @param searchTerm The bid id to search for
 */
vector<Bid> HashTable::Search(string searchTerm) {
    vector<Bid> bids;
	unsigned int hashKey = hash(searchTerm);

	int i = 0;
	for (Bid j : bunchOfBids[hashKey]){
		if (j.bid[sortBy] == searchTerm){
			bids.push_back(bunchOfBids[hashKey].at(i));
			i++;
		}
	}

    return bids;
}

//============================================================================
// User Interface Methods
//============================================================================

/**
 * The one and only main() method
 */
int main(int argc, char* argv[]) {

    // process command line arguments
    string csvPath, bidKey;
    switch (argc) {
    case 2:
        csvPath = argv[1];
        break;
    default:
        csvPath = "eBid_Monthly_Sales_Dec_2016.csv";
    }

    // Define a hash table to hold all the bids
    HashTable* bidTable = new HashTable();
    createHashTable(csvPath, bidTable);

    vector<Bid> bids;

    int choice = 0;
    while (choice != 9) {
        choice = displayMenu();

        switch (choice) {

        case 1:
            bidTable->PrintAll();
            break;

        case 2:
            searchBids(bidTable);
            break;


        case 3:
            removeBids(bidTable);
            break;
        }
    }

    cout << "Good bye." << endl;

    return 0;
}

/**
 * Create a new hash table object sorted based on user input
 *
 * @param csvPath the path to the CSV file to load(for loadBids)
 * @param bidTable the hashTable object to be initialized
 */
void createHashTable(string csvPath, HashTable *bidTable) {

    int choice;
    const string options[] = {
        "bidId",
        "title",
        "closingDate",
        "department",
        "winningBid",
        "fund",
        "inventoryId"
    };
    cout << "Select header to sort by:" << endl;
    cout << "  1. By unique identifier" << endl;
    cout << "  2. By title" << endl;
    cout << "  3. By closing date" << endl;
    cout << "  4. By department" << endl;
    cout << "  5. By winning bid" << endl;
    cout << "  6. By fund" << endl;
    cout << "  6. By inventory identifier" << endl;
    cout << "Enter choice: ";
    cin >> choice;

    bidTable->setSortBy(options[choice-1]);


    // Complete the method call to load the bids
    loadBids(csvPath, bidTable);

}

/**
 * Display main menu to the user and get their choice
 *
 * @return user inputed choice
 */
int displayMenu () {
    int choice;
    cout << "Menu:" << endl;
    cout << "  1. Display All Bids" << endl;
    cout << "  2. Find Bid" << endl;
    cout << "  3. Remove Bid" << endl;
    cout << "  9. Exit" << endl;
    cout << "Enter choice: ";
    cin >> choice;
    return choice;
}

/**
 * Load a CSV file containing bids into the hash table
 *
 * @param csvPath the path to the CSV file to load
 * @param hashTable a container holding all the bids read
 */
void loadBids(string csvPath, HashTable* hashTable) {
    cout << "Loading CSV file " << csvPath << endl;

    // initialize the CSV Parser using the given path
    csv::Parser file = csv::Parser(csvPath);

    // read and display header row
    vector<string> header = file.getHeader();
    for (auto const& c : header) {
        cout << c << " | ";
    }
    cout << "" << endl;

    try {
        // loop to read rows of a CSV file
        for (unsigned int i = 0; i < file.rowCount(); i++) {

            // Create a data structure and add to the collection of bids
            Bid bid(
                file[i][1],
                file[i][0],
                file[i][3],
                file[i][2],
                file[i][4],
                file[i][8],
                file[i][5]
            );

            // push this bid to the end
            hashTable->Insert(bid);
        }
    } catch (csv::Error &e) {
        std::cerr << e.what() << std::endl;
    }
}

/**
 * Remove bids based on user's search term
 *
 * @param bidTable the hash table containing the bids
 */
void removeBids(HashTable* bidTable) {
    string searchKey;
    cout << "Enter search term to remove";
    cin >> searchKey;
    vector<Bid> bids = bidTable->Remove(searchKey);
    printOperationResult(bids);
}

/**
 * Look for bids based on user's search term
 *
 * @param bidTable the hash table containing the bids
 */
void searchBids(HashTable* bidTable) {
    string searchKey;
    cout << "Enter search term to look for";
    cin >> searchKey;
    vector<Bid> bids = bidTable->Search(searchKey);
    printOperationResult(bids);
}

/**
 * Show the results of search and remove operations
 *
 * @param bids which contains the list of bids found
 * matching the search term
 */
void printOperationResult(vector<Bid> bids) {
    if (bids.size() != 0) {
        cout << "Operation successful" << endl;
        for (Bid bid: bids){
            bid.displayBid();
        }
    } else {
        cout << "Search Term not found." << endl;
    }
}
