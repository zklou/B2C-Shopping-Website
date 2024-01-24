import json
import sys

from flask import Flask, request, redirect, Response

import requests

import time, random, sys


app = Flask(__name__)

global sessions
sessions = {}

api_base = 'http://127.0.0.1:8080'

def as_page(content):
    return '''<!DOCTYPE html>
<html lang="en-US">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width" />
    <title>Online Shop</title>
  </head>
  <body>
    ''' + content + '''
     <div id="float_image"><img src=""></div>
     <script>
    let current_display = ''
    let float_div = null

    function on_mouse_enter(o) {
        console.error('on_mouse_enter')
        current_display = o.getAttribute('image_binding')
        float_div.style.display = 'block'
        float_div.innerHTML = '<img style="max-width: 480px;" src="' + current_display + '">'
    }

    function on_mouse_leave() {
        console.error('on_mouse_leave')
        document.getElementById('float_image').style.display = 'None'
    }

    document.addEventListener('mousemove', (event) => {
        if(float_div == null) {
            float_div = document.getElementById('float_image')
        }
        float_div.style.position = 'absolute'
        float_div.style.left = event.clientX + 10 + 'px'
        float_div.style.top = event.clientY + 10 + 'px'
    })

  </script>
  </body>
</html>'''


@app.before_request
def before_request():
    session_key = request.cookies.get('Session', None)
    if session_key is None:
        session = requests.session()
        session.get(api_base + '/')
        session_key = session.cookies.get('Session')
        sessions[session_key] = session

    request.cookies = { 'Session': session_key }


@app.after_request
def after_request(response):
    session_key = request.cookies.get('Session', None)
    response.set_cookie('Session', session_key)
    return response



def link(href, name=None):
    if name is None: name = href
    return f'<a href="{href}">{name}</a>'

def space():
    return '&nbsp;'

def float_image(name, image_url):
    return '<a onmouseenter = "on_mouse_enter(this)" onmouseleave = "on_mouse_leave()" image_binding = "' + image_url + '" href="' + image_url + '">' + name + '</a>'

def get_link_prefix(type_id=-1, brand_id=-1):
    if type_id != -1:
        return f'/type/{type_id}'
    elif brand_id != -1:
        return f'/brand/{brand_id}'
    else:
        return ''


@app.route('/brand/<int:brand_id>/page/<int:page>')
@app.route('/brand/<int:brand_id>')
@app.route('/type/<int:type_id>/page/<int:page>')
@app.route('/type/<int:type_id>')
@app.route('/page/<int:page>')
@app.route('/')
def root(page=1, type_id=-1, brand_id=-1):
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    lines = []

    lines.append('Welcome to OnlineGameShop!')
    lines.append(link('/choose_type') + space() * 4 + link('/choose_brand'))

    lines.append(space())
    lines.append(space())

    response = None

    if type_id != -1:
        response = session.get(api_base + '/products' + '/type' + '/' + str(type_id) + '/page' + '/' + str(page - 1))
    elif brand_id != -1:
        response = session.get(api_base + '/products' + '/brand' + '/' + str(brand_id) + '/page' + '/' + str(page - 1))
    else:
        response = session.get(api_base + '/products' + '/page' + '/' + str(page - 1))

    o = json.loads(response.content.decode())

    for item in o:
        images = item['images']
        image = ''
        if len(images) > 0:
            image = images[0]

        lines.append(
            link(f'/product/' + str(item['id']), item['name']) + space() * 4 +
            'Price: ' + str(item['price'] / 100) + space() * 4 +
            float_image('[Image preview]', image + '?' + str(random.randint(0, sys.maxsize)))
        )

    lines.append(space())
    lines.append(space())

    page_line = ''

    for i in range(10):
        page_line += link(get_link_prefix(type_id, brand_id) + '/page/' + str(i + 1), f'[{i + 1}]')
        page_line += space()

    lines.append(page_line)

    lines.append(space())
    lines.append(space())

    response = session.get(api_base + '/user')

    if response.status_code == 500:
        lines.append(link('/login') + space() * 4 + link('/register'))
    else:
        o = json.loads(response.content.decode())
        lines.append(
            f'Hello {o["first_name"]} {o["last_name"]}' + space() * 4 + link('/cart') + space() * 4 + link('/logout')
        )

    return as_page('<br>'.join(lines))


@app.route('/choose_type')
@app.route('/choose_type/<int:type_id>')
def choose_type(type_id=-1):
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    if type_id != -1:
        return redirect(f'/type/{type_id}')

    lines = []

    response = session.get(api_base + '/categories')
    o = json.loads(response.content.decode())

    for item in o['types']:
        parts = item.split('::')
        lines.append(link('/choose_type' + '/' + str(parts[1]), parts[0]))

    return as_page('<br>'.join(lines))


@app.route('/choose_brand')
@app.route('/choose_brand/<int:brand_id>')
def choose_brand(brand_id=-1):
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    if brand_id != -1:
        return redirect(f'/brand/{brand_id}')

    lines = []

    response = session.get(api_base + '/categories')
    o = json.loads(response.content.decode())

    for item in o['brands']:
        parts = item.split('::')
        lines.append(link('/choose_brand' + '/' + str(parts[1]), parts[0]))

    return as_page('<br>'.join(lines))

def form(action, fields):
    lines = []

    lines.append(f'<form action="{action}" method="post">')

    for field in fields:
        type = 'text'
        if 'password' in field:
            type = 'password'

        lines.append(f'<label for="{field}">{fields[field]}</label>' + space())
        lines.append(f'<input type="{type}" name="{field}">')
        lines.append(space())

    lines.append(f'<input type="submit" Value="Submit">')

    lines.append('</form>')

    return '<br>'.join(lines)


@app.route('/login', methods=['GET', 'POST'])
def login():
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    lines = []
    if request.method == 'GET':
        lines.append(
            form('/login', {
                'username': 'Username:',
                'password': 'Password'
            })
        )
    else:
        response = session.post(api_base + '/session', json={
            'username': request.form.get('username'),
            'password': request.form.get('password')
        })

        if response:
            return redirect('/')
        else:
            lines.append('Login error!')

    return as_page('<br>'.join(lines))

@app.route('/register', methods=['GET', 'POST'])
def register():
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    lines = []
    if request.method == 'GET':
        lines.append(
            form('/register', {
                'username': 'Username:',
                'password': 'Password',
                'email': 'E-Mail',
                'firstName': 'First Name',
                'lastname': 'Last Name'
            })
        )
    else:
        response = session.put(api_base + '/user', json={
            'username': request.form.get('username'),
            'password': request.form.get('password'),
            'email': request.form.get('email'),
            'firstName': request.form.get('firstName'),
            'lastname': request.form.get('lastname')
        })

        if response:
            return redirect('/login')
        else:
            lines.append('Register error!')

    return as_page('<br>'.join(lines))

@app.route('/logout')
def logout():
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    response = session.delete(api_base + '/session')

    print(response.content)

    return as_page('<br>'.join(['You have logout.']))

@app.route('/product/<int:product_id>')
def product(product_id):
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    response = session.get(api_base + '/categories')
    o = json.loads(response.content.decode())

    types_mapping = {}
    brands_mapping = {}

    for item in o['types']:
        parts = item.split('::')
        types_mapping[parts[1]] = parts[0]

    for item in o['brands']:
        parts = item.split('::')
        brands_mapping[parts[1]] = parts[0]


    response = requests.get(api_base + '/product' + '/' + str(product_id))
    o = json.loads(response.content.decode())

    lines = []

    print(brands_mapping)

    lines.append('Name:' + space() + o['name'])
    lines.append('Brand:' + space() + brands_mapping[str(o['brand'])])
    lines.append('Type:' + space() + types_mapping[str(o['type'])])
    lines.append('Description:' + space() + o['description'])
    lines.append('Price:' + space() + str(o['price'] / 100))

    for image in o['images']:
        lines.append(float_image(image, image + f'?{str(random.randint(0, sys.maxsize))}'))

    lines.append('')

    lines.append(link(f'/add_to_cart/{o["id"]}', 'Add to cart'))

    return as_page('<br>'.join(lines))

@app.route('/add_to_cart/<int:product_id>')
def add_to_cart(product_id):
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    session.put(api_base + '/cart', json={'product_id': product_id})

    return redirect('/cart')

@app.route('/cart/<int:product_id>/delete')
def delete_cart_item(product_id):
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session
    
    session.delete(api_base + f'/cart/{product_id}')

    return redirect('/cart')


@app.route('/cart')
def cart():
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    response = session.get(api_base + '/cart')
    o = json.loads(response.content.decode())

    lines = []

    lines.append(space() + '======= Cart =======')
    lines.append(space())

    for item in o['products']:
        if item == '':
            continue

        response = requests.get(api_base + '/product' + '/' + str(item))
        po = json.loads(response.content.decode())

        lines.append(
            link('/product' + '/' + str(po['id']), po['name']) + space() * 4 + link(f'/cart/{str(item)}/delete', '[ - ]')
        )

    lines.append(space())

    lines.append(link('/') + space() * 4 + link('/order'))

    return as_page('<br>'.join(lines))

@app.route('/order', methods=['GET', 'POST'])
def order():
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    if request.method == 'GET':
        lines = []

        lines.append('Please enter your information for putting this order:')
        lines.append(space())

        lines.append(form(
            '/order',
            {
                'credit_card_number': 'Credit Card Number',
                'cvc': 'CCV',
                'expiration_time': 'Expiration Time(MM/YY)',
                'address': 'Address'
            }
        ))

        lines.append(link('/put_order'))

        return as_page('<br>'.join(lines))

    else:
        credit_card_number = request.form['credit_card_number']
        cvc = request.form['cvc']
        expiration_time = request.form['expiration_time']
        address = request.form['address']
        products = []

        response = session.get(api_base + '/cart')
        o = json.loads(response.content.decode())

        for product_id in o['products']:
            products.append(product_id)

        order_request = {
            'credit_card_number': credit_card_number,
            'cvc': cvc,
            'expiration_time': expiration_time,
            'address': address,
            'products': products
        }

        response = session.post(api_base + '/check_out', json=order_request)

        session.post(api_base + '/cart/clear')

        lines = []
        lines.append('OK')
        lines.append(link('/'))

        return as_page('<br>'.join(lines))


import datetime


@app.route('/admin/sales_report')
@app.route('/admin/sales_report/<int:year>/<int:month>')
def sales_report(year=-1, month=-1):
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    today = datetime.date.today()
    parts = str(today).split('-')
    if year == -1:
        year = int(parts[0])
    if month == -1:
        month = int(parts[1])

    response = session.get(api_base + f'/admin/sales_report/{year}/{month}')

    return Response(json.dumps(json.loads(response.content.decode()), indent=4), mimetype='application/json')


@app.route('/admin/access_report')
@app.route('/admin/access_report/<int:year>/<int:month>')
def access_report(year=-1, month=-1):
    session_key = request.cookies.get('Session')
    session = None

    if session_key in sessions:
        session = sessions[session_key]
    else:
        session = requests.session()
        sessions[session_key] = session

    today = datetime.date.today()
    parts = str(today).split('-')
    if year == -1:
        year = int(parts[0])
    if month == -1:
        month = int(parts[1])

    response = session.get(api_base + f'/admin/access_report/{year}/{month}')

    return Response(json.dumps(json.loads(response.content.decode()), indent=4), mimetype='application/json')



if __name__ == '__main__':
    app.run(port=9999, debug=True)
