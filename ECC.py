import random
import hashlib
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad

# Elliptic Curve Parameters
a = 2
b = 3
p = 13
g = (3, 6)


def is_point_on_curve(point):
    x, y = point
    return (y ** 2) % p == (x ** 3 + a * x + b) % p


def point_addition(P, Q):
    if P == Q:
        return point_doubling(P)
    x1, y1 = P
    x2, y2 = Q
    if x1 == x2 and y1 != y2:
        return None
    slope = (y2 - y1) * pow(x2 - x1, -1, p) % p
    xr = (slope ** 2 - x1 - x2) % p
    yr = (slope * (x1 - xr) - y1) % p
    return xr, yr


def point_doubling(P):
    x, y = P
    slope = (3 * x ** 2 + a) * pow(2 * y, -1, p) % p
    xr = (slope ** 2 - 2 * x) % p
    yr = (slope * (x - xr) - y) % p
    return xr, yr


def scalar_multiplication(k, P):
    result = None
    addend = P
    while k:
        if k & 1:
            result = point_addition(result, addend) if result else addend
        addend = point_doubling(addend)
        k >>= 1
    return result


def generate_private_key():
    return random.randint(1, p - 1)


def generate_public_key(private_key):
    return scalar_multiplication(private_key, g)


def derive_shared_secret(private_key, public_key):
    return scalar_multiplication(private_key, public_key)


def hash_shared_secret(secret_point):
    secret_x = secret_point[0]
    return hashlib.sha256(str(secret_x).encode()).digest()


def encrypt_message(public_key, message):
    ephemeral_private_key = generate_private_key()
    ephemeral_public_key = generate_public_key(ephemeral_private_key)
    shared_secret = derive_shared_secret(ephemeral_private_key, public_key)
    shared_key = hash_shared_secret(shared_secret)
    cipher = AES.new(shared_key, AES.MODE_CBC)
    ciphertext = cipher.encrypt(pad(message.encode(), AES.block_size))
    return ciphertext, cipher.iv, ephemeral_public_key

def decrypt_message(private_key, ciphertext, iv, ephemeral_public_key):
    shared_secret = derive_shared_secret(private_key, ephemeral_public_key)
    shared_key = hash_shared_secret(shared_secret)
    cipher = AES.new(shared_key, AES.MODE_CBC, iv)
    plaintext = unpad(cipher.decrypt(ciphertext), AES.block_size)
    return plaintext.decode()


def run_ecdh(alice_private_key, bob_public_key):
    alice_shared_secret = derive_shared_secret(alice_private_key, bob_public_key)
    return alice_shared_secret


def run_ecies(bob_public_key, bob_private_key):
    message = input("Enter the message to be encrypted: ")
    ciphertext, iv, ephemeral_public_key = encrypt_message(bob_public_key, message)
    print("Ciphertext:", ciphertext)
    decrypted_message = decrypt_message(bob_private_key, ciphertext, iv, ephemeral_public_key)
    print("Decrypted Message:", decrypted_message)


def main():
    # Generate Alice's keys
    alice_private_key = generate_private_key()
    alice_public_key = generate_public_key(alice_private_key)

    # Generate Bob's keys
    bob_private_key = generate_private_key()
    bob_public_key = generate_public_key(bob_private_key)

    print("Alice's Private Key:", alice_private_key)
    print("Alice's Public Key:", alice_public_key)
    print("Bob's Private Key:", bob_private_key)
    print("Bob's Public Key:", bob_public_key)

    # Choose operation
    print("\nChoose an option:")
    print("1: Elliptic Curve Diffie-Hellman Key Exchange (ECDH)")
    print("2: Elliptic Curve Integrated Encryption Scheme (ECIES)")

    choice = input("Enter your choice (1 or 2): ")

    if choice == '1':
        alice_shared_secret = run_ecdh(alice_private_key, bob_public_key)
        bob_shared_secret = run_ecdh(bob_private_key, alice_public_key)

        print("Alice's Shared Secret:", alice_shared_secret)
        print("Bob's Shared Secret:", bob_shared_secret)

        if alice_shared_secret == bob_shared_secret:
            print("Key exchange successful! Shared secret is identical.")
        else:
            print("Key exchange failed! Shared secret mismatch.")

    elif choice == '2':
        run_ecies(bob_public_key, bob_private_key)

    else:
        print("Invalid choice. Please enter 1 or 2.")


if __name__ == "__main__":
    main()
