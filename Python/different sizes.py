import time
import matplotlib.pyplot as plt

class CaesarCipher:
    ALPHABET = "abcdefghijklmnopqrstuvwxyz"

    def encrypt(self, plain_text, shift_key):
        plain_text = plain_text.lower()
        cipher_text = ""
        for char in plain_text:
            if char in self.ALPHABET:
                char_position = self.ALPHABET.index(char)
                key_val = (shift_key + char_position) % 26
                replace_val = self.ALPHABET[key_val]
                cipher_text += replace_val
            else:
                cipher_text += char
        return cipher_text

    def decrypt(self, cipher_text, shift_key):
        cipher_text = cipher_text.lower()
        plain_text = ""
        for char in cipher_text:
            if char in self.ALPHABET:
                char_position = self.ALPHABET.index(char)
                key_val = (char_position - shift_key) % 26
                plain_text += self.ALPHABET[key_val]
            else:
                plain_text += char
        return plain_text

def read_from_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        return file.read().strip()

cc = CaesarCipher()
sizes = []
durations = []
print("Encryption time on Size for Caesar Cipher ")
shift_key = int(input("Enter shift key :"))
for i in range(1, 11):
    plain_text = read_from_file(f"PlainEngSample_{i}.txt")
    start = time.perf_counter()
    cipher_text = cc.encrypt(plain_text, shift_key)
    end = time.perf_counter()
    duration = end - start
    encrypted_file_size = len(plain_text.encode('utf-8'))
    sizes.append(encrypted_file_size)
    durations.append(duration)
print(sizes, "\n", durations)
sorted_indices = sorted(range(len(sizes)), key=lambda k: sizes[k])
sizes = [sizes[i] for i in sorted_indices]
durations = [durations[i] for i in sorted_indices]
print(sizes, "\n", durations)
plt.figure(figsize=(10, 6))
plt.plot(sizes, durations, marker='o', color='red', label='Encryption Time')
plt.title(f'Input Size vs Encryption Time when key = {shift_key}')
plt.xlabel('Input Size (characters)')
plt.ylabel('Time (seconds)')
plt.grid(True)
plt.legend()
plt.show()