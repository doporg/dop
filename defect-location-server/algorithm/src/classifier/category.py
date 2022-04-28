import csv
import re


class Category:
    """
    represents a category used to categorize commits.
    """
    associatedWords = []  # all words associated w/ this category
    category_name = None  # name of category

    def __init__(self, file_location, name):
        """
        constructor
        reads in all associated words w/ this category from specified
        file location
        """
        self.category_name = name
        self.associatedWords = []  # reset the instance so that class name is visible to self reference
        self.read_associated_words(file_location)

    def read_associated_words(self, file_location):
        """
        reads in all associated words w/ this category
        """
        with open(file_location, 'rt', encoding='utf-8') as csvfile:
            wordreader = csv.reader(csvfile, delimiter=',', quotechar='|')
            for row in wordreader:
                for word in row:
                    self.associatedWords.append(word)

    def belongs(self, commit_msg):
        """
        checks if a commit belongs to this category by analyzing
        its commit message.
        @return boolean
        """
        # if self.category_name == "Corrective":
        #     patterns = [r'TO #\d+',r'FIX #\d+']
        #     for pattern in patterns:
        #         if re.search(pattern,commit_msg,re.IGNORECASE) != None:
        #             return True
        commit_msg = commit_msg.lower().split(" ")  # to array
        # need to go beyond list contains i.e. fixed = fix
        for word in commit_msg:
            for assoc_word in self.associatedWords:
                if assoc_word in word:
                    return True
        # No associated words found!
        return False

    def get_name(self):
        """
        returns the name of the category
        """
        return self.category_name
