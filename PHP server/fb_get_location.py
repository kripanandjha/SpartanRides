#!/usr/bin/python

import os
import json
import facebook
import numpy as np
import MySQLdb
import re
import threading


token = 'EAACEdEose0cBAJqIx8cNhECBEad4TmhdlY4ZBiQ77ZBApA0FgpA6CcJKIaqonK1foGfdHUF8Bj9SFn1YKXU2cBIZBFR9lkgiGWELgcv6lZCnyZAPnlLUcqnPzYiYTUi2XjYFhJbOiBjdrQbs3Pu5vhaJsJnWMIXaZBrocDKQc9QkottU1J7Cj1ZCicaXgbdae0ZD'

graph = facebook.GraphAPI(token)

profile = graph.get_object('me', fields = 'name, location')
location = profile['location']
location = location['id']
# create location folder it is does not exist
if not os.path.exists(location):
    profile = graph.get_object('me', fields = 'name, location, likes, friends')
    os.makedirs(location)


#print(json.dumps(profile, indent = 4))

profile_id = profile['id']
user_file = location + '/' + profile_id
#print('profile id ', profile_id)


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


# now load data from the user_file into numpy array
user_data = np.loadtxt(user_file, dtype=str)



#execute mysql query to fetch fb user id after the current date
#select FBID  from post1 where date <= curdate();
conn = MySQLdb.connect("localhost", "root", "#B@rahmanand1", "spartans")
c = conn.cursor()
c.execute("select FBID  from post1 where date <= curdate()")
row = c.fetchall()
st = str(row)
st = st.replace("(", " ")
st = st.replace(")", " ")
st = st.replace(",", " ")
st = st.replace(",", " ")
st = st.replace("'", " ")
l1 = st.split()
np_post =  np.array(l1)
#st = re.sub("[^\d\.]", " ", st)
#for eacrhrow in row:
#print type(user_data)
#print type(np_post)
set_a = set(np_post) # post set
set_b = set(user_data) # logged in user info
#print (set(user_data).intersection(np_post))
final_data = set(set_a).intersection(set_b)
not_common = final_data.symmetric_difference(set_a)
final = np.array(final_data)
not_common = list(not_common)
not_common = np.array(not_common)
not_common = np.delete(not_common, 0)
print(not_common)
#now extracting the data from the posted user data and store into numpy array
np_post_data = None
user_len = len(not_common)
set_a = set_b
final_post = np.zeros(user_len)
t = []
for i in range(0, user_len):
	if os.path.exists(location+'/'+not_common[i]):
		np_post_data[i] = np.loadtxt(location+'/'+not_common[i], dtype=str)
		thread = threading.Thread(target=compare, args=(i,))
		t.append(thread)
#for i in range(0, user_len):
#	print(i)
#	t[i].start()

for i in t:
	i.start()
for i in t:
	i.join()
# These below code should be run in multithreading environment
#print(np_post
def compare(num):
	set_a = set(np_post_data[num])
	final_data = set(set_a).intersection(set_b)
	if not bool(final_data):
		#add this user id to and array
		final_post[num] = not_common[i]

#for i in range(0, user_len):
#	t[i] = threading.Thread(target=compare, args=(i,))
print('after returning from all threads')
print(final_post)
