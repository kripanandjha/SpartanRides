#!/usr/bin/python

import os
import json
import facebook
import numpy

token = 'EAACEdEose0cBAF2KhigSemoD2OMXheGP1pPratNa03D0eqvnMB1cRJ3eU0yyjx47ONcayU8zVqZBHmOOtnKpozxuA1PtK3h6MPyqt3ouD6tAK16CZCqN7n0X2UlHL9ht3NAUPddvc2397ATfqeENjrlviJIgNGDHoDSSg4zkBLpexSwS8yD8m690BlZAS8ZD'
graph = facebook.GraphAPI(token)

profile = graph.get_object('me', fields = 'name, location, likes, friends')

#print(json.dumps(profile, indent = 4))

location = profile['location']
location = location['id']

profile_id = profile['id']
user_file = location + '/' + profile_id
#print('profile id ', profile_id)

# create location folder it is does not exist
if not os.path.exists(location):
    os.makedirs(location)

# create userid file if it is does not exist
if not os.path.exists(user_file):
	myfile = open(user_file, 'w')
	friends = profile['friends']
	frnds_id = friends['data']
	# write friends, likes id into the file
	for row in range(len(frnds_id)):
		#print(frnds_id[row]['id'])
		myfile.write(frnds_id[row]['id']+'\n')

	likes = profile['likes']
	likes = likes['data']
	for row in range(len(likes)):
		#print(likes[row]['id'])
		myfile.write(likes[row]['id']+'\n')

	myfile.close()
