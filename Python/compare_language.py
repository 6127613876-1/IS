import time
import string
import random
import matplotlib.pyplot as plt

mal = "അആഇഈഉഊഋഎഏഐഒഓഔകഖഗഘങചഛജഝഞടഠഡഢണതഥദധനപഫബഭമയരലവശഷസഹളക്ഷഴറ"

def generate_ciphertext(length=100):
    letters = string.ascii_lowercase
    return ''.join(random.choice(letters) for i in range(length))

def generate_malciphertext(length=100):
    return ''.join(random.choice(mal) for i in range(length))

def brute_force_attack(ciphertext):
    start_time = time.perf_counter()
    for key in string.ascii_lowercase:
        decrypted_text = ''.join(chr((ord(char) - ord(key)) % 26 + ord('a')) for char in ciphertext)
        if decrypted_text == ciphertext:
            break
    end_time = time.perf_counter()
    return end_time - start_time

def mallu_brute_force_attack(ciphertext):
    start_time = time.perf_counter()
    for key in mal:
        decrypted_text = ''.join(mal[(mal.index(char) - mal.index(key)) % len(mal)] for char in ciphertext)
        if decrypted_text == ciphertext:
            break
    end_time = time.perf_counter()
    return end_time - start_time

def frequency_analysis_attack(ciphertext):
    start_time = time.perf_counter()
    frequency = {char: ciphertext.count(char) for char in set(ciphertext)}
    most_frequent = max(frequency, key=frequency.get)
    key = (ord(most_frequent) - ord('e')) % 26
    decrypted_text = ''.join(chr((ord(char) - key) % 26 + ord('a')) for char in ciphertext)
    end_time = time.perf_counter()
    return end_time - start_time

def mallu_frequency_analysis_attack(ciphertext):
    start_time = time.perf_counter()
    frequency = {char: ciphertext.count(char) for char in set(ciphertext)}
    most_frequent = max(frequency, key=frequency.get)
    key = (mal.index(most_frequent) - mal.index('ജ')) % len(mal)
    decrypted_text = ''.join(mal[(mal.index(char) - key) % len(mal)] for char in ciphertext)
    end_time = time.perf_counter()
    return end_time - start_time

# Generate samples
samples = [generate_ciphertext() for _ in range(10)]
mallu_samples = [generate_malciphertext() for _ in range(10)]

# Perform attacks
bruteforce = [brute_force_attack(sample) for sample in samples]
mallu_bruteforce = [mallu_brute_force_attack(mallu_sample) for mallu_sample in mallu_samples]

frequencyanalysis = [frequency_analysis_attack(sample) for sample in samples]
mallu_frequencyanalysis = [mallu_frequency_analysis_attack(mallu_sample) for mallu_sample in mallu_samples]

# Plot results
plt.plot(range(1, 11), bruteforce, label='Brute-force Attack for English')
plt.plot(range(1, 11), frequencyanalysis, label='Frequency Analysis Attack for English')
plt.plot(range(1, 11), mallu_bruteforce, label='Brute-force Attack for Malayalam')
plt.plot(range(1, 11), mallu_frequencyanalysis, label='Frequency Analysis Attack for Malayalam')
plt.xlabel('Sample Number')
plt.ylabel('Time (seconds)')
plt.title('Time Taken for Brute-force vs Frequency Analysis Attack')
plt.legend()
plt.show()
