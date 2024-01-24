import time

from flask import Flask, Response, send_file
from flask_cors import CORS

app = Flask(__name__)

cors = CORS(app, resources={r"/*": {"origins": "*"}})






'''

- Register

PUT
/user
{
    username: ...
    email: ...
    password: ...
    firstName: ...
    lastName: ...
}

!!! generic response

============================================================

- Sign in

POST
/session
{
    'username': ...,
    'password': ...
}

!!! generic response

Response should have a Set-Cookie header, browser will handle it

============================================================


- categories
GET 
/categories
{
    'types': [
        'type_1::type1_id',
        'type_2::type2_id',
        'type_3::type3_id',
    ],
    
    'brands': [
        'brand_1::brand_id',
        'brand_2::brand_id',
        'brand_3::brand_id',
    ]
}

============================================================



List catalogue items
- Filter catalog items by type

GET
OLD: /products/type/{type_name}/page/{page_number}
NEW: /products/type/{type_id}[/page/{page_number}]
{
    products: [
        {
            id: ...,
            name: ...,
            price: ...  # price is represent as integer, should divide by 100 when display
            images: [...] #urls
        }
    ]
}

============================================================

- Filter catalog items by brand

GET
OLD: /products/type/{brand_name}/page/{page_number}
NEW: /products/brand/{brand_id}[/page/{page_number}]
{
    products: [
        {
            id: ...,
            name: ...,
            price: ...  # price is represent as integer, should divide by 100 when display
            images: [...] #urls
        }
    ]
}

============================================================

- list all product

GET
/products/page/{page_number}

{
    products: [
        {
            id: ...,
            name: ...,
            price: ...,  # price is represent as integer, should divide by 100 when display
            images: [...] #urls
        }
    ]
}

============================================================



- View details of a product

GET
/product/{product_id}

{
    id: ...,
    name: ...,
    brand: ...,
    type: ...,
    description: ...,
    price: ...,  # price is represent as integer, should divide by 100 when display
    images: [...] #urls,
}

When display details, should RE-USE data from data from */products/*, and also request /product/{product_id}/review to get reviews

============================================================


- Add items to shopping cart

PUT
/cart
{
    'product_id': id
}

# response: 
{
    'products' [product_id...]
}

============================================================

- list products in cart

GET
/cart
{
    'products': [...] # a array of product_id
}

============================================================

- Edit or remove items from the shopping cart

DELETE
/cart/{product_id}

!!! generic response



============================================================


- “Check out” by providing credit card information and shipping information to purchase the
items in the shopping cart

POST
/check_out
{
    'credit_card_number': ...,
    'cvc': ...,
    'expiration_time': MM/YY,
    'address': ...,
    'products': [...] # a array of product_id
}

if products is empty array

!!! generic response

============================================================




- list orders

GET
/orders
{
    orders: [
        {
            id: ...
            time: ...
            address: ...
            product_list_id: ...
        }
    ]
}

============================================================

- get products on list

GET
/product_list/{product_list_id}

{
    products: [product_id...]
}

============================================================



- Write reviews on items

PUT
/product/{product_id}/review
{
    'rating': 1 -> 5,
    'content': review content
}

!!! generic response

============================================================

- Read reviews on items

GET
/product/{product_id}/review
{
    'average_rating': 5.0,
    'reviews': [
        {
            'display_name': ...,
            'date': timestamp,
            'rating': 1 -> 5,
            'content': review content
        },
        {
            'display_name': ...,
            'date': timestamp,
            'rating': 1 -> 5,
            'content': review content
        }...
    ]
}

!!! generic response

============================================================














































GET
/admin/sales_report/{YYYY}/{MM}
{
    'report': [
        {
            user_id:
            product_id:
            date:
        },
        {
            user_id:
            product_id:
            date:
        }
    ]
}

============================================================

GET
/admin/access_report/{YYYY}/{MM}
{
    'report': [
        {
            path: ...,
            visit_count: ...,
        },
        {
            path: ...,
            visit_count: ...,
        }
    ]
}
============================================================


- Clear items in cart

POST
/cart/clear

!!! generic response

============================================================

- Sign out

DELETE
/session

!!! generic response

Backend will disconnect the connection between the user account and session id, 
you can delete the cookie "Session", but backend will give you new one
it's up to you.


************************************************************
************************************************************
************************************************************

About (generic response)

generic response format:
{
    "message": ...
}


Should pay attention to the response's status code
2xx means operation completed successfully
4xx, 5xx means error happends, error reason will be in "message" field

Backend is still under developing, but may not be huge change.

************************************************************
************************************************************
************************************************************

@GetMapping("/admin/sales_report/{year}/{month}")
@GetMapping("/admin/access_report/{year}/{month}")
@GetMapping("/cart")
@PutMapping("/cart")
@DeleteMapping("/cart/{product_id}")
@PostMapping("/cart/clear")
@PostMapping("/check_out")
@GetMapping("/orders")
@GetMapping("/product_list/{product_list_id}")
@GetMapping("/")
@GetMapping("/probe")
@GetMapping("/probe_need_login")
@GetMapping("/categories")
@GetMapping(value = {"/products/type/{type_id}/page/{page_number}", "/products/type/{type_id}"})
@GetMapping(value = {"/products/brand/{brand_id}/page/{page_number}", "/products/brand/{brand_id}"})
@GetMapping(value = {"/products", "/products/page/{page_number}"})
@GetMapping(value = {"/product/{product_id}" })
@PutMapping("/user")
@PostMapping("/session")
@DeleteMapping("/session")

'''

import random
import math
import string
import sys
import time

from flask import request


def random_string(length=8):
    result = ''
    while len(result) <= length:
        result += random.choice(string.ascii_letters + string.digits)

    return result


def build_products_list(list_type='', page='1'):
    result = []
    for i in range(random.randint(0, 10) + 2):
        result.append(
            {
                'id': abs(random.randint(0, sys.maxsize)),
                'name': list_type + '_' + random_string(8) + '_' + page,
                'price': abs(random.randint(0, sys.maxsize)),
                'images': ['https://pic.re/image', 'https://pic.re/image', 'https://pic.re/image']
            }
        )

    return result


@app.route('/products')
@app.route('/products/page/<page_number>')
@app.route('/products/type/<brand_name>')
@app.route('/products/type/<brand_name>/page/<page_number>')
@app.route('/products/type/<type_name>')
@app.route('/products/type/<type_name>/page/<page_number>')
def products(brand_name=None, type_name=None, page_number=1):
    list_type = None
    if brand_name is not None: list_type = brand_name
    if type_name is not None: list_type = type_name
    return build_products_list(str(list_type), str(page_number))


@app.route('/categories')
def categories():
    types = []
    brands = []

    for i in range(0, random.randint(1, 10)):
        types.append(f'type_{random_string()}::{random.randint(0, sys.maxsize)}')

    for i in range(0, random.randint(1, 10)):
        types.append(f'brand_{random_string()}::{random.randint(0, sys.maxsize)}')

    return {
        'types': types,
        'brands': brands
    }


def make_generic_response(message='', status=200):
    return Response({'message': message}, status=status)


@app.route('/product/<product_id>')
def product(product_id=-1):
    if product_id == -1:
        return make_generic_response('need product_id', 500)

    return {
        'id': product_id,
        'name': f'{product_id}_{random_string(8)}',
        'price': abs(random.randint(0, sys.maxsize)),
        'brand': random_string(4),
        'type': random_string(4),
        'description': random_string(32),
        'images': ['https://pic.re/image', 'https://pic.re/image', 'https://pic.re/image']
    }


import json


@app.route('/cart', methods=['GET', 'PUT'])
def cart():
    last_one = []
    if request.method == 'PUT':
        o = json.loads(request.data)
        last_one.append(o['product_id'])

    return {
        'products': [random_string(8) for _ in range(random.randint(2, 10))] + last_one
    }


@app.route('/cart/<product_id>', methods=['DELETE'])
def cart_delete(product_id):
    return make_generic_response(f'{product_id} has been deleted.', 200)


def check_field(json_str, field_name):
    o = json.loads(json_str)
    check_result = field_name in o
    return f'Field ${field_name}$ check ... ' + {True: 'OK', False: 'Fail'}[check_result] + '\n'


@app.route('/check_out', methods=['POST'])
def check_out():
    message = str(request.data)
    message += '\n'
    message += check_field(request.data, 'credit_card_number')
    message += check_field(request.data, 'cvc')
    message += check_field(request.data, 'expiration_time')
    message += check_field(request.data, 'address')

    return make_generic_response(message, 200)


@app.route('/orders')
def orders():
    result = {
        'orders': []
    }

    for i in range(random.randint(10)):
        result['orders'].append({
            'id': random.randint(0, sys.maxsize),
            'time': random.randint(0, int(time.time() * 1000)),
            'address': random_string(32),
            'product_list_id': random.randint(0, sys.maxsize)
        })

    return result


@app.route('/product_list/<product_list_id>')
def product_list(product_list_id):
    return {
        'products': [random.randint(0, sys.maxsize) for _ in range(2 + random.randint(10))] + [product_list_id]
    }


@app.route('/product/<product_id>/review', methods=['GET', 'PUT'])
def product_review(product_id):
    to_append = []
    if request.method == 'PUT':
        o = json.loads(request.data)
        o['display_name'] = random_string(8)
        o['date'] = random.randint(0, int(time.time() * 1000))
        to_append.append(o)

    result = {'reviews': [], 'average_rating': random.randint(0, 10) / 2}

    for i in range(random.randint(2, 10)):
        result['reviews'].append(
            {
                'display_name': random_string(8),
                'date': random.randint(0, int(time.time() * 1000)),
                'rating': 5,
                'content': random_string(32)
            }
        )

    return result


@app.route('/user', methods=['PUT'])
def register():
    message = str(request.data)
    message += '\n'
    message += check_field(request.data, 'username')
    message += check_field(request.data, 'email')
    message += check_field(request.data, 'password')
    message += check_field(request.data, 'firstName')
    message += check_field(request.data, 'lastName')

    return make_generic_response(message, 200)


@app.route('/session', methods=['POST', 'PUT'])
def session():
    message = str(request.data)
    message += '\n'

    if request.method == 'POST':
        message += check_field(request.data, 'username')
        message += check_field(request.data, 'password')

        return make_generic_response(message, 200)
    else:  # request.method == 'PUT'
        return make_generic_response('You have logout.', 200)


@app.route('/admin/sales_report/<year>/<month>')
def sales_report(year, month):
    result = {'report': []}

    for i in range(2, random.randint(3, 10)):
        result['report'].append({
            'user_id': random.randint(0, 50000),
            'product_id': random.randint(0, 50000),
            'date': random.randint(0, int(time.time() * 1000))
        })

    return result


@app.route('/admin/access_report/<year>/<month>')
def access_report(year, month):
    result = {'report': []}

    for i in range(2, random.randint(3, 10)):
        result['report'].append({
            'path': random_string(16),
            'visit_count': random.randint(0, 50000)
        })

    return result


@app.route('/')
def root():
    return send_file('mock.py', as_attachment=False)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=7070)
