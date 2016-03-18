# ZenHub-stat

A sample web application for interview process.


## Getting started

### Requirements

 * [Typesafe activator 1.3.7](https://www.lightbend.com/activator/download)
 * [git](https://git-scm.com/downloads)

### First run

```bash
git clone https://github.com/Isammoc/zenhub-stat.git
cd zenhub-stat
activator run
```


## TODO

### Required

1. [ ] A user should be able to search for a public project (also called repository) hosted on the GitHub platform.

2. [ ] The user will choose one of the projects returned by the search.

3. [ ] Your application would show three basic types of analytics for the selected project:
  * [ ] List of committers (users) for this project.
  * [ ] Based on the 100 latest commits, the impact of each user on the project (based on number of commits)
  * [ ] Based on the 100 latest commits, the projection of commits on a timeline.

4. [ ] This result page must be bookmark-able for later direct access.

### Optional

* [ ] Adding new kinds of analytics - if you think that manipulating data is fun.

* [ ] Extending and polishing the user interface (Ajax, auto-suggest, animated results; ...) if you’re a UI guy.

* [ ] Exposing these results through an API - if software design is your passion.
