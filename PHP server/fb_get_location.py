#!/usr/bin/python

import os
import json
import facebook
import numpy as np
import MySQLdb
import re
import threading

def compare(num):
	set_a = set(np_post_data[num])
	final_data = set(set_a).intersection(set_b)
	if not bool(final_data):
		#add this user id to and array
		global final_post
		final_post[num] = not_common[i]


#pass token from the php script
token = sys.argv[1]
#token = 'EAACEdEose0cBAIh49qnEuxoXETe02Xt6acZBhSVPYZAHZCrqzrhaR0lYIidafIiv9zx1h0y7KJ0azXTnNeYn80gxo9u7lai5fuSYEhvOZAqJd0wvKWutwITGCwVJzlgkpz4TnqSFYLhItFyfocG117sYmKZA1iOXej3587cIZBZCxsZCHwm45C60Guvj1e7sEKcZD'

token = sys.argvp[1] # facebook access token sent by the server page
graph = facebook.GraphAPI(token)

profile = graph.get_object('me', fields = 'name, location')
#print profile
location = profile['location']
location = location['id']
# create location folder it is does not exist
if not os.path.exists(location):
    #print profile
    os.makedirs(location)


#print(json.dumps(profile, indent = 4))

profile_id = profile['id']
user_file = location + '/' + profile_id
#print('profile id ', profile_id)


# create userid file if it is does not exist
if not os.path.exists(user_file):
    	profile = graph.get_object('me', fields = 'name, location, likes, friends')
	#print profile
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
conn = MySQLdb.connect("localhost", "user", "password", "database")
c = conn.cursor()
c.execute("select FBID from post1 where date <= curdate()")
row = c.fetchall()
conn.close()
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
#print (np_post)
set_a = set(np_post) # post set
set_b = set(user_data) # logged in user info
#print (set(user_data).intersection(np_post))
common_data = set(set_a).intersection(set_b)

# logged in user and posted users are friend show those post first
# add those user into final list
not_common = common_data.symmetric_difference(set_a)
#final = np.array(common_data)
not_common = list(not_common)
not_common = np.array(not_common)
not_common = np.delete(not_common, 0)
#print(not_common)
#ab = ','.join(not_common)
#print(ab)
#now extracting the data from the posted user data and store into numpy array
user_len = len(not_common)
#[[np_post_data for x in xrange(10)] for y in range(user_len)]
np_post_data = [['0']*16]*user_len
set_a = set_b
final_post = np.zeros(user_len, dtype=str)
#print(user_len)
#print(final_post)
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
#for i in range(0, user_len):
#	t[i] = threading.Thread(target=compare, args=(i,))
#print('after returning from all threads')
final_post = np.trim_zeros(final_post)
final_post = np.setdiff1d(final_post,'')
#print((final_post))

#print((common_data))
#common_data = list(common_data)
#if final_post
xx = list(common_data) + list(final_post)
#print(type(xx))
#ids = ','.join(xx)
#print(ids)


# now query mysql with the given final post id

conn = MySQLdb.connect("localhost", "user", "password", "database")
c = conn.cursor()
#print(list(xx))
#c.execute("select FBID from post1 where date <= curdate()")
#c.execute("SELECT username, email, ph_url, FBID, source, destination, date, time  FROM post1 where FBID in ("+",".join("%s",)*len(xx))+")", list(xx))
c.execute("SELECT username, email, ph_url, FBID, source, destination, date, time  FROM post1 where FBID in ('%s')" % "','".join(xx) )

row = c.fetchall()

conn.close()

def send_data():
	global row
	return json.dumps(row)

send_data()

