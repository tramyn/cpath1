import urllib
import timeit

baseUrl = "http://localhost:8080/cpath/"
#baseUrl = "http://cbio.mskcc.org/cpath/"

# Hit the home page to init the web app
def hitHomePage():
    print "Hitting home page to init web app..."
    url = baseUrl + "cpath/home.do?debug=1"
    f = urllib.urlopen(url)
    s = f.read()
    f.close

# Hit the Web API;  do not check XML cache
def hitWebAPI(useOptimizedCode):
    print "Hit Web API"
    url = baseUrl + "webservice.do?version=1.0&cmd=get_by_keyword&q=protein&format=psi_mi&maxHits=100&checkXmlCache=0"
    if (useOptimizedCode):
	url += "&useOptmizedCode=1"
    else:
	url += "&useOptimizedCode=0"
    f = urllib.urlopen(url)
    s = f.read()
    #print s
    f.close

hitHomePage()

print "Test performance of web API (execute old, crappy code)"
t = timeit.Timer("hitWebAPI(0)", "from __main__ import hitWebAPI")
numTrials = 10
totalTime = 0.0
trials = t.repeat(numTrials, 1)
for trial in trials:
    print "Trial %0.2f ms" % (1000.0 * trial)
    totalTime += trial
average = totalTime / numTrials
print "Average: %0.2f ms" % (1000.0 * average)

print "Test performance of web API (execute new code)"
t = timeit.Timer("hitWebAPI(1)", "from __main__ import hitWebAPI")
numTrials = 10
totalTime = 0.0
trials = t.repeat(numTrials, 1)
for trial in trials:
    print "Trial %0.2f ms" % (1000.0 * trial)
    totalTime += trial
average = totalTime / numTrials
print "Average: %0.2f ms" % (1000.0 * average)

