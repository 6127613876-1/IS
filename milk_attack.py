import hashlib
import itertools
import time
import matplotlib.pyplot as plt
import random

def hash_password(password):
    return hashlib.sha256(password.encode()).hexdigest()

def create_dictionary(char_set, max_size):
    word_dict = {}
    for combo in itertools.product(char_set, repeat=max_size):
        word = ''.join(combo)
        word_dict[word] = hash_password(word)
    return word_dict

def launch_dict_attack(word_dict, password_table):
    username = 'user'
    hashed_password = password_table[username]
    for word, hashed_word in word_dict.items():
        if hashed_word == hashed_password:
            return word
    return None

def draw_plot(x_data, y_data, x_title, y_title):
    plt.plot(x_data, y_data, marker='o', color='red', linestyle='-')
    plt.xlabel(x_title)
    plt.ylabel(y_title)
    plt.title(f'{x_title} vs {y_title}')
    plt.grid(True)
    plt.show()

def charset_vs_time(charset):
    pin_size = 4
    cur_set = ""
    time_taken = []
    sizes = [i + 1 for i in range(len(charset))]
    for char in charset:
        cur_set += char
        word_dict = create_dictionary(cur_set, pin_size)
        random_pin = "".join(random.choice(cur_set) for _ in range(pin_size))
        password_table = {'user': hash_password(random_pin)}
        start_time = time.time()
        launch_dict_attack(word_dict, password_table)
        end_time = time.time()
        time_taken.append((end_time - start_time) * 1000)  # Convert to milliseconds
    print(time_taken)
    draw_plot(sizes, time_taken, x_title='Character Set Size', y_title='Time taken (ms)')

def create_password_table():
    password_table = {}
    while True:
        username = input("Enter username (or type 'done' to finish): ")
        if username == 'done':
            break
        password = input("Enter password: ")
        password_table[username] = hash_password(password)
    return password_table


def display_password_table(password_table):
    for username, hashed_password in password_table.items():
        print(f"Username: {username}, Hashed Password: {hashed_password}")


def dictionary_attack(username, password_table, word_dict):
    if username not in password_table:
        print("Username not found.")
        return None

    hashed_password = password_table[username]
    for word, hashed_word in word_dict.items():
        if hashed_word == hashed_password:
            return word
    return None


def plot_time_vs_length(lengths, times):
    plt.plot(lengths, times, marker='o', color='red', linestyle='-')
    plt.xlabel('Password Length')
    plt.ylabel('Time taken (milliseconds)')
    plt.title('Password Length vs Time taken')
    plt.grid(True)
    plt.show()


def hell():
    char_set = input("Enter character set (e.g., abcdefghijklmnopqrstuvwxyz): ")
    length = int(input("Enter password length (e.g., 5): "))

    while True:
        print("\nMenu:")
        print("1. Create dictionary of all possible combinations and their hashes")
        print("2. Create password table")
        print("3. Display password table")
        print("4. Launch dictionary attack")
        print("5. Plot Time vs Password Length")
        print("6. Plot Time vs Character Set Size")
        print("7. Exit")
        choice = input("Enter your choice: ")

        if choice == '1':
            word_dict = create_dictionary(char_set, length)
            print("Dictionary created.")
        elif choice == '2':
            password_table = create_password_table()
            print("Password table created.")
        elif choice == '3':
            if 'password_table' in locals():
                display_password_table(password_table)
            else:
                print("Password table not created yet.")
        elif choice == '4':
            username = input("Enter username to attack: ")
            if 'password_table' in locals() and 'word_dict' in locals():
                start_time = time.time()
                password = dictionary_attack(username, password_table, word_dict)
                end_time = time.time()
                if password:
                    print(f"Password found: {password}")
                else:
                    print("Password not found.")
                print(f"Time taken: {(end_time - start_time) * 1000:.2f} milliseconds")
                plot_time_vs_length([length], [(end_time - start_time) * 1000])
            else:
                print("Dictionary or password table not created yet.")
        elif choice == '5':
            lengths = [i for i in range(1, 6)]
            length_times = []
            for length in lengths:
                word_dict = create_dictionary(char_set, length)
                random_pin = ''.join(random.choice(char_set) for _ in range(length))
                password_table = {'user': hash_password(random_pin)}
                start_time = time.time()
                launch_dict_attack(word_dict, password_table)
                end_time = time.time()
                length_times.append((end_time - start_time) * 1000)
            plot_time_vs_length(lengths, length_times)
        elif choice == '6':
            charset_vs_time(char_set)
        elif choice == '7':
            break
        else:
            print("Invalid choice. Please try again.")

hell()