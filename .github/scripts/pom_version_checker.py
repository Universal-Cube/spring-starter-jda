import re
import subprocess
import xml.etree.ElementTree as ET
import os


def get_pom_version():
    try:
        tree = ET.parse("pom.xml")
        root = tree.getroot()
        version = root.find("{http://maven.apache.org/POM/4.0.0}version").text
        return version
    except FileNotFoundError:
        print("pom.xml not found.")
        return None
    except Exception as e:
        print(f"Error parsing pom.xml: {e}")
        return None


def get_latest_tag():
    try:
        result = subprocess.run(
            ["git", "describe", "--tags", "--abbrev=0"],
            capture_output=True,
            text=True,
            check=True,
        )
        return result.stdout.strip()
    except subprocess.CalledProcessError:
        print("No Git tags found.")
        return None
    except Exception as e:
        print(f"Error retrieving last Git tag: {e}")
        return None


def create_tag(version):
    tag_name = f"v{version}"
    try:
        subprocess.run(
            ["git", "tag", tag_name], capture_output=True, text=True, check=True
        )
        print(f"Tag '{tag_name}' created successfully.")
    except subprocess.CalledProcessError as e:
        print(f"Error during tag creation: {e}")
    except Exception as e:
        print(f"Unexpected error during tag creation: {e}")


def push_tag(tag_name):
    try:
        subprocess.run(
            ["git", "config", "user.name", "github-actions[bot]"],
            capture_output=True,
            text=True,
            check=True,
        )
        subprocess.run(
            [
                "git",
                "config",
                "user.email",
                "github-actions[bot]@users.noreply.github.com",
            ],
            capture_output=True,
            text=True,
            check=True,
        )
        subprocess.run(
            ["git", "push", "origin", tag_name],
            capture_output=True,
            text=True,
            check=True,
        )
        print(f"Tag '{tag_name}' pushed successfully.")
    except subprocess.CalledProcessError as e:
        print(f"Error when pushing tag: {e.stderr}")
    except Exception as e:
        print(f"Unexpected error during tag push: {e}")


if __name__ == "__main__":
    pom_version = get_pom_version()
    latest_tag = get_latest_tag()

    if pom_version and latest_tag:
        if f"v{pom_version}" != latest_tag:
            create_tag(pom_version)
            push_tag(f"v{pom_version}")
    elif pom_version and latest_tag is None:
        create_tag(pom_version)
        push_tag(f"v{pom_version}")
