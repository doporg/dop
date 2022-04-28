from classifier.category import *
import os 


class Classifier:
    categories = [] # array of possible commit categories

    def __init__(self):
        current_dir = os.path.dirname(__file__)
        dir_of_cats = current_dir + '/Categories'
        corrective = Category(dir_of_cats + "/corrective.csv", "Corrective")
        feature_addition = Category(dir_of_cats + "/feature_addition.csv", "Feature Addition")
        non_functional = Category(dir_of_cats + "/non_functional.csv", "Non Functional")
        perfective = Category(dir_of_cats + "/perfective.csv", "Perfective")
        perventive = Category(dir_of_cats + "/preventative.csv", "Preventative")
        # add to list of categories
        self.categories.extend([corrective,feature_addition,non_functional,perfective,perventive])

    def categorize(self, commit_msg):
        for category in self.categories:
            if category.belongs(commit_msg):
                return category.get_name()
        return "None"

