#!/usr/bin/env python
import re
import sys
import mechanize

CHK_ULR = 'http://dhunews.sinaapp.com/howareyou'
USERNAME = ''
PASSWORD = ''
if not USERNAME or not PASSWORD:
    print 'Please specify your username and password in the source file'
    sys.exit(1)
debug = False

br = mechanize.Browser()
br.set_handle_robots(False)
br.addheaders = [('User-agent', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.1.11) Gecko/20100701 Firefox/3.5.11')]

def check_online():
    resp = br.open(CHK_ULR)
    if resp.info().get('X-How-Are-You') != 'fine':
        return resp.geturl(), None
    return None, resp.read()

def login(login_url):
    br.open(login_url)
    for form in br.forms():
        try:
            form.find_control(type='password').value = USERNAME
            form.find_control(type='text', predicate=lambda c: re.search('u.*name', c.name, re.I)).value = PASSWORD
            br.form = form
            resp = br.submit()
            if debug:
                print resp.read()
            break
        except mechanize._form.ControlNotFoundError:
            pass
    else:
        if debug:
            print 'No login form found'

if __name__ == '__main__':
    if '-v' in sys.argv:
        debug = True
    login_url, quote = check_online()
    if quote:
        print quote
    else:
        login(login_url)
        login_url, quote = check_online()
        quote = quote or 'Login failed'
        print quote

