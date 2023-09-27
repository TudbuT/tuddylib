[![Codacy Badge](https://app.codacy.com/project/badge/Grade/b57be0d44d5c4a10a22124f5815cc2bb)](https://www.codacy.com/gh/TudbuT/tuddylib/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=TudbuT/tuddylib&amp;utm_campaign=Badge_Grade)

# tuddylib


TuddyLIB's package hell has been unified.

To convert your own code, run `bash -c "find src -type f -print0 | while IFS= read -d \$'\0' s ; do (cat \"\$s\" | sed -E 's/([^.])tudbut\./\1de.tudbut./g' > \"\$s.tmp\") && mv \"\$s.tmp\" \"\$s\" && echo processed \"\$s\" ; done"`