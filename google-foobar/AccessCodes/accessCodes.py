def answer(x):
    out = [sorted(list(y.lower())) for y in x]
    notDupes=[]

    while len(out) != 0:
        item = out.pop()
        notDupes.append(item)
        if item in out:
            out = list(filter(item.__ne__, out))

    return len(notDupes)

def main():
    x = ["foo", "bar", "", "rab",'oof'] #["x", "y", "xy", "yy", "", "yx"] #
    q = answer(x)
    print(q)
if __name__ == "__main__":
    main()
