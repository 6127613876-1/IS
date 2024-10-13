import random

a = 2
b = 3
p = 13
g = (3, 6)  # Generator point


def point_on_curve(point):
    if point is None:
        return False
    x, y = point
    return (y ** 2) % p == (x ** 3 + a * x + b) % p


def point_double(P):
    if P is None:
        return None
    x, y = P
    slope = (3 * x ** 2 + a) * pow(2 * y, -1, p) % p
    x3 = (slope ** 2 - 2 * x) % p
    y3 = (slope * (x - x3) - y) % p
    return x3, y3


def point_add(P, Q):
    if P is None:
        return Q
    if Q is None:
        return P

    x1, y1 = P
    x2, y2 = Q
    if x1 == x2 and y1 != y2:
        return None  # Point at infinity
    if P == Q:
        return point_double(P)

    slope = (y2 - y1) * pow(x2 - x1, -1, p) % p
    x3 = (slope ** 2 - x1 - x2) % p
    y3 = (slope * (x1 - x3) - y1) % p
    return x3, y3


def generatepri():
    return random.randint(1, p - 1)


def generatepub(priv_key):
    pub_key = scalar_mul(priv_key, g)
    if point_on_curve(pub_key):
        return pub_key
    else:
        return None  # Return None if the public key is not valid


def scalar_mul(k, P):
    result = None
    addend = P

    while k:
        if k & 1:
            result = point_add(result, addend)
        addend = point_double(addend)
        k >>= 1
    return result


def sharedkey(priv_key, pub_key):
    if pub_key is None:
        return None
    return scalar_mul(priv_key, pub_key)


def main():
    if not point_on_curve(g):
        print("Generator point is not on the curve!")
        return

    alpv = generatepri()
    alpu = generatepub(alpv)

    bobpr = generatepri()
    bobpub = generatepub(bobpr)

    print("Alice's Private Key:", alpv)
    print("Alice's Public Key:", alpu)
    print("Bob's Private Key:", bobpr)
    print("Bob's Public Key:", bobpub)

    if alpu is None or bobpub is None:
        print("Public key generation failed! Invalid point on the curve.")
        return

    alsh = sharedkey(alpv, bobpub)
    bobsh = sharedkey(bobpr, alpu)

    print("Alice's Shared Key:", alsh)
    print("Bob's Shared Key:", bobsh)

    if alsh == bobsh:
        print("Key exchange successful! Shared secret is identical.")
    else:
        print("Key exchange failed! Shared secret mismatch.")

main()
